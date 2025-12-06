package com.jamuara.crs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamuara.crs.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String failureReason;

    private String amount;
//
//    private String hash;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();
}
