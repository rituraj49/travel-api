package com.jamuara.crs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class HotelReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String kcUserId;

    private String supplierBookingId;

    private String bookingStatus;

//    private Instant bookingDate;

    private String hotelId;
    private String hotelName;

    private String checkInDate;
    private String checkOutDate;

    private Integer roomCount;

    private String email;
    private String phone;

    private String totalPrice;

    private String currency;

    private String supplier;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String bookingRequest;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String bookingResponse;

    @OneToMany(mappedBy = "hotelReservation", cascade = CascadeType.ALL)
    private List<HotelGuest> guests;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = true)
    @JsonIgnore
    private UserProfile userProfile;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private TravelPackage travelPackage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
