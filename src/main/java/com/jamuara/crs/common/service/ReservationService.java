package com.jamuara.crs.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.common.repository.ReservationRepository;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.TboApiFetchFlightBookingResponseDto;
import com.jamuara.crs.model.Reservation;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@Profile("!nodb")
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    public void saveReservation(String bookingId, String price, String currencyCode, String source, String destination, String traveler_name, String email, String phoneNo , Reservation.BookingStatus bookingStatus, String bookingResponseJson){
//        Reservation reservation = new Reservation(bookingId,price,currencyCode,source,destination,traveler_name,email,phoneNo,bookingStatus,bookingResponseJson);
//        reservationRepository.save(reservation);
    }

    public void createReservation(FlightBookingResponse bookingResponse) throws JsonProcessingException {
        String bookingId = bookingResponse.getOrderId();
        bookingId = URLDecoder.decode(bookingId, StandardCharsets.UTF_8);
        String price = bookingResponse.getFlightOffer().getTotalPrice();
        String currencyCode = bookingResponse.getFlightOffer().getCurrencyCode();
        String source = bookingResponse.getFlightOffer().getTrips().get(0).getFrom();

        //for multi city search
        int legs = bookingResponse.getFlightOffer().getTrips().size();

        String destination=null;
        if(legs>1) {
            destination=bookingResponse.getFlightOffer().getTrips().get(legs-1).getTo();
        } else {
            destination=bookingResponse.getFlightOffer().getTrips().get(0).getTo();
        }
        //String destination=bookingResponse.getFlightOffer().getTrips().get(0).getTo();
        String travelerName = bookingResponse.getTravelers().get(0).getFirstName()+" "+bookingResponse.getTravelers().get(0).getLastName();
        String email = bookingResponse.getTravelers().get(0).getEmail();
        String phoneNo = bookingResponse.getTravelers().get(0).getPhones().get(0).getNumber();
        Reservation.BookingStatus bookingStatus = Reservation.BookingStatus.CONFIRM;
        String bookingResponseJson = objectMapper.writeValueAsString(bookingResponse);
        saveReservation(bookingId,price,currencyCode,source,destination,travelerName,email,phoneNo,bookingStatus,bookingResponseJson);
    }
//
//    public List<Reservation> findAllReservationByName(String name) {
//        return reservationRepository.findReservationByTravelerNameContainingIgnoreCase(name);
//    }

    public Reservation findByBookingId(String bookingId) throws NotFoundException {
        Reservation res = reservationRepository.findReservationByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        if(res == null) {
            throw new NotFoundException("reservation not found");
        }

        return res;
    }

    @Transactional
    public Reservation findByBookingIdWithAllRelations(String bookingId) throws NotFoundException {
//        return reservationRepository.findReservationWithAllRelations(bookingId)
        Reservation r = reservationRepository.findReservationByBookingId(bookingId)
                .orElseThrow(() -> new NotFoundException("reservation not found"));
        int t = r.getTravelers().size();
        int f = r.getFlightLegs().size();
        return r;
    }

    public List<Reservation> findReservationsByPaymentId(Long paymentId) {
        return reservationRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new NotFoundException("no reservations found for payment id: " + paymentId));
    }

    public Reservation createReservationTbo(FetchFlightBookingResponse response, Reservation.BookingStatus status, TboApiFetchFlightBookingResponseDto rawResponse) throws JsonProcessingException {
        Reservation res = reservationMapper.toReservation(response);
        res.setBookingStatus(status);
        res.setBookingResponse(objectMapper.writeValueAsString(rawResponse));

        return this.reservationRepository.save(res);
    }

    public Page<Reservation> findReservationsByStatus(Reservation.BookingStatus status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reservationRepository.findReservationByBookingStatus(status, pageable);
    }

    public Page<Reservation> findAllReservations(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reservationRepository.findAll(pageable);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}