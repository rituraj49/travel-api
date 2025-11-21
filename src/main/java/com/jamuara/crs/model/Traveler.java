package com.jamuara.crs.model;

import com.jamuara.crs.enums.Gender;
import com.jamuara.crs.enums.TravelerType;
import com.jamuara.crs.flight.dto.tbo.book.FetchFlightBookingResponse;
import com.jamuara.crs.flight.dto.tbo.book.TravelerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Traveler {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String travelerId;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private TravelerType travelerType;

    private Boolean leadTraveler;

    private String phoneCountryCode;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
