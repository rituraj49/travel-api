package com.jamuara.crs.model;

import com.jamuara.crs.flight.dto.FlightBookingRequest;
import com.jamuara.crs.flight.dto.FlightBookingResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Profile("!nodb")
public class UserLogs {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String username;

    private LocalDateTime logTimestamp = LocalDateTime.now();

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private FlightBookingRequest requestPayload;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private FlightBookingResponse responsePayload;

    private Integer numberOfTravellers;

    private String totalAmount;

    private String currencyCode;
    private String fromLocation;

    private String toLocation;

    public UserLogs(String orderId, LocalDateTime logTimestamp, String username, FlightBookingRequest requestPayload,
                    FlightBookingResponse responsePayload, Integer numberOfTravellers,
                      String totalAmount, String fromLocation, String toLocation,String currencyCode) {
        this.orderId = orderId;
        this.logTimestamp = logTimestamp;
        this.username = username;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.numberOfTravellers = numberOfTravellers;
        this.totalAmount = totalAmount;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.currencyCode=currencyCode;
    }

    @PrePersist
    public void setLogTimestamp() {
        if (logTimestamp == null) {
            this.logTimestamp = LocalDateTime.now();
        }
    }




}
