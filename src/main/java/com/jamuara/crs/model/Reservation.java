package com.jamuara.crs.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;

    private String pnr;

    private boolean isLcc;

    private boolean domestic;

    private String price;

    private String lastTicketDate;

    private String currencyCode;

    private String origin;

    private String destination;

    @Column(name = "traveler_first_name")
    private String travelerFirstName;

    @Column(name = "traveler_last_name")
    private String travelerLastName;

    private String email;

    private String phone;

    private String countryCallingCode;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

//    @Lob
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String bookingResponse;
/*

    public Reservation(String bookingId, String price, String currencyCode, String source, String destination, String travelerName, String email, String phoneNo) {
    }
*/

    public Reservation(String bookingId, String price, String currencyCode, String origin, String destination, String traveler_name, String email, String phone, BookingStatus bookingStatus, String bookingResponse) {
        this.bookingId = bookingId;
        this.price = price;
        this.currencyCode = currencyCode;
        this.origin = origin;
        this.destination = destination;
        this.travelerFirstName = traveler_name;
        this.email = email;
        this.phone = phone;
        this.bookingStatus = bookingStatus;
        this.bookingResponse=bookingResponse;
    }

    public enum BookingStatus{
        CONFIRM,
        PENDING,
        CANCEL
    }


    public Reservation() {
    }
}
