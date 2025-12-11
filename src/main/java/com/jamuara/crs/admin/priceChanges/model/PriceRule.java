package com.jamuara.crs.admin.priceChanges.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class PriceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;        // Weekend, Rainy, Festival
    private Double percentage;    // 10, 5, -5 (increase or discount)

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean active;

}
