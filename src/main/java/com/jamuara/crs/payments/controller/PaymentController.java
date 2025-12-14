package com.jamuara.crs.payments.controller;

import com.jamuara.crs.enums.CallbackResult;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.service.FlightBookingAsyncService;
import com.jamuara.crs.payments.dto.InitiatePaymentRequestDto;
import com.jamuara.crs.payments.service.PaymentService;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jamuara.crs.enums.PaymentStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    FlightBookingAsyncService flightBookingAsyncService;

    @Value("${frontend.host.url}")
    String frontendUrl;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody InitiatePaymentRequestDto requestDto) {
        try {
            log.info("initiate payment request: {}", requestDto.toString());
            Map<String, Object> payuReq = paymentService.createPaymentIntent(requestDto);
            return ResponseEntity.ok().body(payuReq);
        } catch (Exception e) {
            log.error("An internal error occurred while initiating payment : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/success-redirect")
    public ResponseEntity<?> successUrl(@RequestParam("txnid") String txnid) {
//        log.info("success url req body: " + req.toString());
        log.info("param txnid: " + txnid);
//        String txnid = req.get("txnid");
        String txnidFinal = "";
        if(txnid.contains(",")) {
            txnidFinal = txnid.split(",")[0];
        }
            String html = """
            <html>
                <body>
                    <script>
                        window.location.href = "%s/payment/return?txnid=%s";
                    </script>
                </body>
            </html>
        """.formatted(frontendUrl, txnidFinal);
/*

        String html = """
            <html>
                <body>
                    <script>
                        window.location.href = "http://localhost:5173/payment/return?txnid=%s";
                    </script>
                </body>estonian to english
            </html>
        """.formatted(txnidFinal);
*/

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
    }

    @PostMapping("/failure-redirect")
    public ResponseEntity<?> failureUrl(@RequestBody Map<String, String> req) {
        log.info("failure url req body: " + req.toString());

        String txnid = req.get("txnid");

            String html = """
            <html>
            <h6>redirecting to bookings</h6>
                <body>
                    <script>
                        window.location.href = "%s/payment/failure?txnid=%s";
                    </script>
                </body>
            </html>
        """.formatted(frontendUrl, txnid);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
    }

    @PostMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam Map<String, String> requestBody) {
    log.info("webhook success request body received: {}", requestBody.toString());
        CallbackResult response = paymentService.processPayment(requestBody);
        if(response == CallbackResult.SUCCESS) {
            flightBookingAsyncService.triggerFlightBookingAsync(requestBody.get("txnid"));
            return ResponseEntity.ok("payment processed successfully!");
        }

        if(response == CallbackResult.ALREADY_PROCESSED) {
//            return ResponseEntity.ok("payment processed already!");
            return ResponseEntity.ok("ignored");
        }

        if(response == CallbackResult.INVALID_HASH) {
            return ResponseEntity.badRequest().body("invalid hash");
        }

        if(response == CallbackResult.ERROR) {
            return ResponseEntity.ok().body("ok");
        }

        return ResponseEntity.ok("ok");
//        try {
//            paymentService.processPayment(requestBody);
////        paymentService.bookFlightAfterSuccessfulPayment();
//            return ResponseEntity.ok("payment processed successfully!");
//        } catch(Exception e) {
//            e.printStackTrace();
//            return ResponseEntity
//            .status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body("something went wrong while processing payment: " + e.getMessage());
//        }
    }

    @PostMapping("/failure")
    public ResponseEntity<?> paymentFailure(@RequestParam Map<String, String> requestBody) {
        log.info("webhook failure request body received: {}", requestBody.toString());

        CallbackResult response = paymentService.processPayment(requestBody);
        if(response == CallbackResult.SUCCESS) {
            return ResponseEntity.ok("payment processed successfully!");
        }

        if(response == CallbackResult.ALREADY_PROCESSED) {
//            return ResponseEntity.ok("payment processed already!");
            return ResponseEntity.ok("ignored");
        }

        if(response == CallbackResult.INVALID_HASH) {
            return ResponseEntity.badRequest().body("invalid hash");
        }

        if(response == CallbackResult.ERROR) {
            return ResponseEntity.ok().body("ok");
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/test/success")
    public ResponseEntity<?> paymentSuccessTest(@RequestParam Map<String, String> requestBody) {
    log.info("test webhook success request body received: {}", requestBody.toString());
//        try {

        CallbackResult response = paymentService.processPayment(requestBody);
        if(response == CallbackResult.SUCCESS) {
            flightBookingAsyncService.triggerFlightBookingAsync(requestBody.get("txnid"));
            return ResponseEntity.ok("payment processed successfully!");
        }

        if(response == CallbackResult.ALREADY_PROCESSED) {
//            return ResponseEntity.ok("payment processed already!");
            return ResponseEntity.ok("ignored");
        }

        if(response == CallbackResult.INVALID_HASH) {
            return ResponseEntity.badRequest().body("invalid hash");
        }

        if(response == CallbackResult.ERROR) {
            return ResponseEntity.ok().body("ok");
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/test/failure")
    public ResponseEntity<?> paymentFailureTest(@RequestParam Map<String, String> requestBody) {
        log.info("test webhook failure request body received: {}", requestBody.toString());

        CallbackResult response = paymentService.processPayment(requestBody);
        if(response == CallbackResult.SUCCESS) {
            return ResponseEntity.ok("payment processed successfully!");
        }

        if(response == CallbackResult.ALREADY_PROCESSED) {
//            return ResponseEntity.ok("payment processed already!");
            return ResponseEntity.ok("ignored");
        }

        if(response == CallbackResult.INVALID_HASH) {
            return ResponseEntity.badRequest().body("invalid hash");
        }

        if(response == CallbackResult.ERROR) {
            return ResponseEntity.ok().body("ok");
        }

        return ResponseEntity.ok("ok");
    }

    @GetMapping("/status")
    public ResponseEntity<?> getPaymentStatus(@RequestParam("txnid") String txnid) {
        try {
            log.info("txnid in payment status request: {}", txnid);
            PaymentStatus status = paymentService.getpaymentStatus(txnid);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("something went wrong while fetching payment status: " + e.getMessage());
        }
    }

    @GetMapping("/bookings/{txnid}")
    public ResponseEntity<?> fetchBookingsByPayment(@PathVariable String txnid) {
        try {
            List<FetchFlightBookingResponse> bookings = paymentService.fetchBookingsByPayment(txnid);
            log.info("found bookings: {}", bookings.size());
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("error while fetching bookings for txnid: {} - {}", txnid, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("something went wrong while fetching bookings for txnid: " + e.getMessage());
        }
    }
}
