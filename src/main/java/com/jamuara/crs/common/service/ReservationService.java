package com.jamuara.crs.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import com.jamuara.crs.common.repository.ReservationRepository;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.TboApiFetchFlightBookingResponseDto;
import com.jamuara.crs.model.Reservation;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    public Reservation createReservationTbo(FetchFlightBookingResponse response, Reservation.BookingStatus status, TboApiFetchFlightBookingResponseDto rawResponse) throws JsonProcessingException {
//        Reservation res = new Reservation();

//        res.setTravelerFirstName(response.getTicketBookingDetails().getFlightDetails().getTravelers().get(0).getFirstName());
//        res.setTravelerLastName(response.getTicketBookingDetails().getFlightDetails().getTravelers().get(0).getLastName());
//        res.setEmail(response.getTicketBookingDetails().getFlightDetails().getTravelers().get(0).getEmail());
//        res.setPhone(response.getTicketBookingDetails().getFlightDetails().getTravelers().get(0).getPhone());
//        res.setCountryCallingCode(response.getTicketBookingDetails().getFlightDetails().getTravelers().get(0).getPhoneCountryCode());
//        res.setDestination(response.getTicketBookingDetails().getFlightDetails().getDestination());
//        res.setOrigin(response.getTicketBookingDetails().getFlightDetails().getOrigingetTicketFare());
//        res.setPnr(response.getTicketBookingDetails().getPnr());
//        res.setBookingId(response.getTicketBookingDetails().getBookingId());
//        res.setPrice(response.getTicketBookingDetails().getFlightDetails().getTicketFare().getPublishedFare());
//        res.setCurrencyCode(response.getTicketBookingDetails().getFlightDetails().getTicketFare().getCurrency());
//        res.setLastTicketDate(response.getTicketBookingDetails().getFlightDetails().getLastTicketDate());
//        res.setLcc(response.getTicketBookingDetails().getFlightDetails().isLCC());
//        res.setDomestic(response.getTicketBookingDetails().getFlightDetails().isDomestic());

        Reservation res = reservationMapper.toReservation(response);
        res.setBookingStatus(status);
        res.setBookingResponse(objectMapper.writeValueAsString(rawResponse));

        return this.reservationRepository.save(res);
    }

    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations;
    }

    public List<Reservation> findReservationsByStatus(Reservation.BookingStatus status) {
        return reservationRepository.findReservationByBookingStatus(status);
    }

    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}