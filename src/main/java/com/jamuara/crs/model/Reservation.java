package com.jamuara.crs.model;


import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kcUserId;

    private String bookingId;

    private String pnr;

    private boolean lcc;

    private boolean domestic;

    private String price;

    private String lastTicketDate;

    private String currencyCode;

    private String origin;

    private String destination;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private String txnid;

//    @Lob
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String bookingRequest;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String bookingResponse;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Traveler> travelers = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FlightLeg> flightLegs = new ArrayList<>();

    @ManyToOne(optional = true)
    @JoinColumn(name = "payment_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Payment payment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

//    public Reservation(String bookingId, String price, String currencyCode, String origin, String destination, String traveler_name, String email, String phone, BookingStatus bookingStatus, String bookingResponse) {
//        this.bookingId = bookingId;
//        this.price = price;
//        this.currencyCode = currencyCode;
//        this.origin = origin;
//        this.destination = destination;
//        this.travelerFirstName = traveler_name;
//        this.email = email;
//        this.phone = phone;
//        this.bookingStatus = bookingStatus;
//        this.bookingResponse=bookingResponse;
//    }

    public enum BookingStatus{
        CONFIRM,
        PENDING,
        CANCEL
    }
}