package com.jamuara.crs.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "bookings")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBooking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String customerName;

    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        CANCELLED,
        CONFIRMED
    }

}
