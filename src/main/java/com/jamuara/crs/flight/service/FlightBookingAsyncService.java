package com.jamuara.crs.flight.service;

import com.jamuara.crs.common.repository.PaymentRepository;
import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.enums.PaymentStatus;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingTicketingRequest;
import com.jamuara.crs.model.Payment;
import com.jamuara.crs.model.Reservation;
import com.jamuara.crs.payments.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightBookingAsyncService {
    @Autowired
    private TboFlightService tboFlightService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CacheManager cacheManager;

    @Async
    @Transactional
    public void triggerFlightBookingAsync(String txnid) {
        Payment p = paymentService.findPaymentByTxnid(txnid);
        p.setBookingStatus(Payment.PaymentBookingStatus.IN_PROGRESS);
        paymentRepository.save(p);

        Cache cache = cacheManager.getCache("bookingIntent");

        Cache.ValueWrapper wrapper = cache.get(txnid);

        try {
            FlightBookingTicketingRequest bookingIntent =
                    wrapper != null ? (FlightBookingTicketingRequest) wrapper.get() : null;

            if(bookingIntent == null) {
                throw new IllegalStateException("Booking intent missing for txn id: " + txnid);
            }

            List<FetchFlightBookingResponse> bookings = null;
                bookings = tboFlightService.flightBookAndTicket(bookingIntent);

            log.info("bookings created after successful payment");
            p.setBookingStatus(Payment.PaymentBookingStatus.SUCCESS);
            List<Reservation> reservations = bookings.stream()
                    .map(b ->
                            reservationService.findByBookingId(b.getTicketBookingDetails().getBookingId())).collect(Collectors.toList());

            reservations.forEach(res -> {
                if(p.getStatus().equals(PaymentStatus.SUCCESS)) {
                    log.info("setting payment into reservation");
                    res.setPayment(p);
//                    p.getReservations().add(res);
                    reservationService.saveReservation(res);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().contains("TBO_ERROR")) {
                p.setBookingStatus(Payment.PaymentBookingStatus.FAILURE);
                p.setBookingFailureReason(e.getMessage());

                paymentRepository.save(p);
            }
            log.error("error while flight booking with txnid {} : {}", txnid, e.getMessage());
        }
    }
}
