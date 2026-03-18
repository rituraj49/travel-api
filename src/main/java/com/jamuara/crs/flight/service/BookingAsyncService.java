package com.jamuara.crs.flight.service;

import com.amadeus.exceptions.ResponseException;
import com.jamuara.crs.common.repository.PaymentRepository;
import com.jamuara.crs.common.service.ReservationService;
import com.jamuara.crs.enums.BookingType;
import com.jamuara.crs.enums.PaymentStatus;
import com.jamuara.crs.flight.dto.FlightBookingRequest;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.FlightBookingTicketingRequest;
import com.jamuara.crs.hotel.dto.HotelBookingRequestDto;
import com.jamuara.crs.hotel.service.HotelService;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingAsyncService {
    @Autowired
    private TboFlightService tboFlightService;

    @Autowired
    private AmadeusFlightService amadeusFlightService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private CacheManager cacheManager;

    @Async
    @Transactional
    public void triggerBookingAsync(String txnid) {
        Payment p = paymentService.findPaymentByTxnid(txnid);
        p.setBookingStatus(Payment.PaymentBookingStatus.IN_PROGRESS);
        paymentRepository.save(p);

        Cache cache = cacheManager.getCache("bookingIntent");

        Cache.ValueWrapper wrapper = cache.get(txnid);

        if(p.getBookingType() == BookingType.FLIGHT) {
//            FlightBookingTicketingRequest bookingIntent =
//                    wrapper != null ? (FlightBookingTicketingRequest) wrapper.get() : null;
            FlightBookingRequest bookingIntent = wrapper != null ? (FlightBookingRequest) wrapper.get() : null;

            triggerFlightBooking(bookingIntent, p);
        } else if(p.getBookingType() == BookingType.HOTEL) {
            HotelBookingRequestDto bookingIntent = wrapper != null ? (HotelBookingRequestDto) wrapper.get() : null;
            log.info("cached booking intent found: {}", wrapper.get().toString());
            triggerHotelBooking(bookingIntent);
        }
    }

    public void triggerFlightBookingTbo(FlightBookingTicketingRequest bookingTicketingRequest, Payment p) {
        try {
            if(bookingTicketingRequest == null) {
                throw new IllegalStateException("Booking intent missing for txn id: " + p.getTxnid());
            }

            List<FetchFlightBookingResponse> bookings = null;
            bookings = tboFlightService.flightBookAndTicket(bookingTicketingRequest);

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
            log.error("error while flight booking with txnid {} : {}", p.getTxnid(), e.getMessage());
        }
    }

    public void triggerFlightBooking(FlightBookingRequest bookingTicketingRequest, Payment p) {
        try {
            if(bookingTicketingRequest == null) {
                throw new IllegalStateException("Booking intent missing for txn id: " + p.getTxnid());
            }

            FlightBookingResponse booking = null;
            booking = amadeusFlightService.createFlightOrder(bookingTicketingRequest);

            log.info("bookings created after successful payment");
            p.setBookingStatus(Payment.PaymentBookingStatus.SUCCESS);
            Reservation reservation = reservationService
                    .findByBookingId(booking.getOrderId());

//            reservations.forEach(res -> {
//                if(p.getStatus().equals(PaymentStatus.SUCCESS)) {
                    log.info("setting payment into reservation");
                    reservation.setPayment(p);
//                    p.getReservations().add(res);
//                    reservationService.saveReservation(res);
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().contains("TBO_ERROR")) {
                p.setBookingStatus(Payment.PaymentBookingStatus.FAILURE);
                p.setBookingFailureReason(e.getMessage());

                paymentRepository.save(p);
            }
            log.error("error while flight booking with txnid {} : {}", p.getTxnid(), e.getMessage());
        }
    }

    public void triggerHotelBooking(HotelBookingRequestDto hotelBookingRequest) {
        try {
            hotelService.bookHotel(hotelBookingRequest);
        } catch(Exception e) {
            e.printStackTrace();
            log.error("error while booking hotel: {}", e.getMessage());
        }
    }
}
