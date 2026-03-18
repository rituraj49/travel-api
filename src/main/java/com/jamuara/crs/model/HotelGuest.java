package com.jamuara.crs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class HotelGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int tid;

    private String title;
    private String firstName;
    private String lastName;

    private Integer childAge;

    @ManyToOne
    @JoinColumn(name = "hotel_reservation_id")
    @ToString.Exclude
    @JsonIgnore
    private HotelReservation hotelReservation;
}
