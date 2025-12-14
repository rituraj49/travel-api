package com.jamuara.crs.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.common.Helper;
import com.jamuara.crs.common.repository.PaymentRepository;
import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.enums.CallbackResult;
import com.jamuara.crs.enums.PaymentStatus;
import com.jamuara.crs.flight.dto.tbo.book.FetchBookingRequest;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingTicketingRequest;
import com.jamuara.crs.flight.service.FlightBookingAsyncService;
import com.jamuara.crs.flight.service.TboFlightService;
import com.jamuara.crs.model.Payment;
import com.jamuara.crs.model.Reservation;
import com.jamuara.crs.payments.dto.InitiatePaymentRequestDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentService {
    @Value("${payu.merchant.key}")
    private String key;

    @Value("${payu.merchant.salt}")
    private String salt;

    @Autowired
    private TboFlightService tboFlightService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> createPaymentIntent(InitiatePaymentRequestDto dto) {
        String email = "";
        if(Helper.isUserAuthenticated()) {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            email = jwt != null ? jwt.getClaim("email") : dto.getEmail();
        } else {
            email = dto.getEmail();
        }

        String txnid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        String amount = dto.getAmount();
        String productinfo = "flight_ticket";
        String firstname = dto.getFirstName();
        String phone = dto.getPhone();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";

        String hashString = key + "|" + txnid + "|" + amount + "|" + productinfo + "|" +
                firstname + "|" + email + "|" + udf1 + "|" + udf2 + "|" +
                udf3 + "|" + udf4 + "|" + udf5 + "||||||" + salt;

        String hash = generateHash(hashString);

//        caching the booking request data to later book flight
        cacheBookingIntent(dto.getBookingTicketingRequest(), txnid);

        createPayment(amount, txnid);

        Map<String, Object> response = new HashMap<>();
            response.put("key", key);
            response.put("txnid", txnid);
            response.put("amount", amount);
            response.put("firstname", firstname);
            response.put("email", email);
            response.put("phone", phone);
            response.put("productinfo", productinfo);
            response.put("surl", Helper.getApplicationUrl() + "/payment/success-redirect?txnid="+txnid);
            response.put("furl", Helper.getApplicationUrl() + "/payment/failure-redirect?txnid="+txnid);
            //response.put("surl", "http://localhost:8080/payment/success?txnid="+txnid);
            //response.put("furl", "http://localhost:8080/payment/failure?txnid="+txnid);
           // response.put("surl", "https://api.jamuarasoft.com/payment/test/success?txnid="+txnid);
           // response.put("furl", "https://api.jamuarasoft.com/payment/test/failure?txnid="+txnid);
             //response.put("surl", "https://localhost:8080/payment/test/success?txnid="+txnid);
             //response.put("furl", "https://localhost:8080/payment/test/failure?txnid="+txnid);

        response.put("hash", hash);

        return response;
    }

    private static String generateHash(String hashString) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = md.digest(hashString.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte b: hashBytes) sb.append(String.format("%02x", b));
            System.out.println("generateed hash");
            System.out.println(sb.toString().toLowerCase());
            return sb.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyHash(Map<String, String> request) {
        String key = request.get("key");
        String status = request.get("status");
        String udf5 = request.get("udf5");
        String udf4 = request.get("udf4");
        String udf3 = request.get("udf3");
        String udf2 = request.get("udf2");
        String udf1 = request.get("udf1");
        String email = request.get("email");
        String firstname = request.get("firstname");
        String productinfo = request.get("productinfo");
        String amount = request.get("amount");
        String txnid = request.get("txnid");

        String hashString = salt + "|" + status + "||||||" +
                udf5 + "|" + udf4 + "|" + udf3 + "|" + udf2 + "|" + udf1 + "|" +
                email + "|" + firstname + "|" + productinfo + "|" +
                amount + "|" + txnid + "|" + key;

        String hash = generateHash(hashString);

        return hash.equals(request.get("hash"));
    }

    @Transactional
    public CallbackResult processPayment(Map<String, String> req) {
        Payment p = findPaymentByTxnid(req.get("txnid"));
        log.info("payment record found in database: {}", p.toString());

        if(!verifyHash(req)) {
//            p.setStatus(PaymentStatus.FAILURE);
//            p.setFailureReason("HASH_INVALID");
//            paymentRepository.save(p);
            log.error("hash validation failed");
            return CallbackResult.INVALID_HASH;
//            throw new BadRequestException("invalid hash received from payu, possible tampering");
        }

        String payuStatus = req.get("status");

        if (!"success".equalsIgnoreCase(payuStatus)) {
            if(p.getStatus() != PaymentStatus.FAILURE) {
                p.setStatus(PaymentStatus.FAILURE);
                p.setFailureReason("PAYU_STATUS_" + payuStatus);
                paymentRepository.save(p);
            }
            return CallbackResult.ERROR;
        }

        if (p.getStatus() == PaymentStatus.SUCCESS) {
            log.info("payment exist already, moving on");
            return CallbackResult.ALREADY_PROCESSED;
        }

        log.info("marking payment as successful");
        p.setStatus(PaymentStatus.SUCCESS);
        p.setPayuTxnid(req.get("mihpayid"));

        try {
            p.setPayuResponse(objectMapper.writeValueAsString(req));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        paymentRepository.save(p);

        return CallbackResult.SUCCESS;
    }

//    public void processPayment(Map<String, String> req) throws Exception {
//        Payment p = findPaymentByTxnid(req.get("txnid"));
//
//        log.info("payment record found in database: {}", p.toString());
//
////        if(!verifyHash(req)) {
////            p.setStatus(PaymentStatus.FAILURE);
////            p.setFailureReason("HASH_INVALID");
////            paymentRepository.save(p);
////            log.error("hash validation failed");
////            throw new BadRequestException("invalid hash received from payu, possible tampering");
////        }
//
//        String payuStatus = req.get("status");
//
//        if (!"success".equalsIgnoreCase(payuStatus)) {
//            p.setStatus(PaymentStatus.FAILURE);
//            p.setFailureReason("PAYU_STATUS_" + payuStatus);
//            paymentRepository.save(p);
//            return;
//        }
//
//        if (p.getStatus() == PaymentStatus.SUCCESS && !p.getReservations().isEmpty()) {
//            log.info("payment exist already and reservations are successful, moving on");
//            return;
//        }
//
//        log.info("marking payment as successful");
//        p.setStatus(PaymentStatus.SUCCESS);
//        p.setPayuTxnid(req.get("mihpayid"));
////        paymentRepository.save(p);
//
//        Cache cache = cacheManager.getCache("bookingIntent");
//
//        Cache.ValueWrapper wrapper = cache.get(p.getTxnid());
//
//        FlightBookingTicketingRequest bookingIntent =
//                wrapper != null ? (FlightBookingTicketingRequest) wrapper.get() : null;
//
//        if(bookingIntent == null) {
//            throw new IllegalStateException("Booking intent missing for txn id: " + p.getTxnid());
//        }
//
//        List<FetchFlightBookingResponse> bookings = tboFlightService.flightBookAndTicket(bookingIntent);
//        log.info("bookings created after successful payment");
//        List<Reservation> reservations = bookings.stream()
//                .map(b ->
//                        reservationService.findByBookingId(b.getTicketBookingDetails().getBookingId())).collect(Collectors.toList());
//
////        log.info("marking payment as successful");
////        p.setStatus(PaymentStatus.SUCCESS);
////        p.setPayuTxnid(req.get("mihpayid"));
//
//        reservations.forEach(res -> {
//            if(p.getStatus().equals(PaymentStatus.SUCCESS)) {
//                log.info("setting payment into reservation");
//                res.setPayment(p);
////                reservationService.saveReservation(res);
//            }
//        });
//
//        log.info("setting reservations into payment");
//        p.setReservations(reservations);
//        paymentRepository.save(p);
//    }

    public PaymentStatus getpaymentStatus(String txnid) {
        Payment p = findPaymentByTxnid(txnid);

        return p.getStatus();
    }

    public List<FetchFlightBookingResponse> fetchBookingsByPayment(String txnid) throws Exception {
        log.info("fetching reservations for txnid: {}", txnid);
        Payment p = findPaymentByTxnid(txnid);
        List<Reservation> reservations = new ArrayList<>();
        if(p.getBookingStatus() == Payment.PaymentBookingStatus.SUCCESS) {
            reservations = reservationService.findReservationsByPaymentId(p.getId());
            System.out.println("found reservations: " + reservations.toString());
            if(reservations.isEmpty()) {
                log.info("no reservations found, returning empty list");
                return Collections.emptyList();
            }

            return reservations.stream()
                    .map(r -> fetchSingleBooking(r.getBookingId())).toList();
        } else if (p.getBookingStatus() == Payment.PaymentBookingStatus.FAILURE) {
            throw new Exception(p.getFailureReason());
        } else {
            return Collections.emptyList();
        }
    }

    public FetchFlightBookingResponse fetchSingleBooking(String bookingId) {
        try {
            FetchBookingRequest req = new FetchBookingRequest();
            req.setBookingId(bookingId);
            log.info("fetching single booking for booking id: {}", bookingId);
            return tboFlightService.fetchBookingDetails(req);
        } catch (Exception e) {
            log.error("error while fetching flight booking from tbo flight service: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public void cacheBookingIntent(FlightBookingTicketingRequest request, String txnid) {
        cacheManager.getCache("bookingIntent").put(txnid, request);
    }

    public void createPayment(String amount, String txnid) {
        Payment p = new Payment();
        p.setAmount(amount);
        p.setStatus(PaymentStatus.PENDING);
        p.setTxnid(txnid);
        p.setBookingStatus(Payment.PaymentBookingStatus.PENDING);

        paymentRepository.save(p);
    }

    public Payment findPaymentByTxnid(String txnid) {
        return paymentRepository.findByTxnid(txnid)
                .orElseThrow(() -> new NotFoundException("payment record not found for txn id: " + txnid));
    }
}
