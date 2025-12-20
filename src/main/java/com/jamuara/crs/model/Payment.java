package com.jamuara.crs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamuara.crs.enums.BookingType;
import com.jamuara.crs.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String txnid;

    private String payuTxnid;

    private BookingType bookingType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payuResponse;

    private String failureReason;

    private String amount;

    @Enumerated(EnumType.STRING)
    private PaymentBookingStatus bookingStatus;

    private String bookingFailureReason;
//
//    private String hash;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static enum PaymentBookingStatus{
        SUCCESS,
        PENDING,
        FAILURE,
        IN_PROGRESS
    }
}
