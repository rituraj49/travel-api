package com.jamuara.crs.payments.controller;

import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.payments.dto.InitiatePaymentRequestDto;
import com.jamuara.crs.payments.service.PaymentService;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody InitiatePaymentRequestDto requestDto) {
        try {
            System.out.println("initiate payment reqq: " + requestDto.toString());
            Map<String, Object> payuReq = paymentService.createPaymentIntent(requestDto);
            return ResponseEntity.ok().body(payuReq);
        } catch (Exception e) {
            log.error("An internal error occurred while initiating payment : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/payu-webhook")
    public ResponseEntity<?> paymentSuccess(@RequestBody Map<String, String> requestBody) {
    log.info("webhook request body received: {}", requestBody.toString());
        try {
            paymentService.processPayment(requestBody);
//        paymentService.bookFlightAfterSuccessfulPayment();
            return ResponseEntity.ok("payment processed successfully!");
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("something went wrong while processing payment: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getPaymentStatus(@RequestParam("txnid") String txnid) {
        try {
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
            e.printStackTrace();
            log.error("error while fetching bookings for txnid: {} - {}", txnid, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("something went wrong while fetching bookings for txnid: " + e.getMessage());
        }
    }
}
