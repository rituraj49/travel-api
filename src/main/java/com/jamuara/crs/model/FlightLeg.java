package com.jamuara.crs.model;

import com.jamuara.crs.enums.TravelClass;
import com.jamuara.crs.enums.TripType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FlightLeg {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private int legNo;

    private int tripNo;

    private String carrierCode;

    private String carrierName;

    private String operatingCarrier;

    private String flightNumber;

    private String aircraftCode;

    private String departureAirport;

    private String departureTerminal;

    private String departureDateTime;

    private String arrivalAirport;

    private String arrivalDateTime;

    private String duration;

    private String layoverDuration;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
