package com.jamuara.crs.travel_package.dto;

import com.jamuara.crs.flight.dto.TravelerRequestDto;
import com.jamuara.crs.hotel.dto.HotelBookingRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TravelPackageRequestDto {
    public boolean hasFlights;
    public boolean hasHotels;
    public boolean hasActivities;

    public String flightOffer;
    public List<TravelerRequestDto> travelers;

    public HotelBookingRequestDto hotel;

    public static class HotelPayload {
        public List<Guest> guests;
        public TravelAgent travelAgent;
        public List<RoomAssociation> roomAssociations;
        public Payment payment;
    }

    public static class TravelerDetails {
        public String id;
        public String dateOfBirth;
        public String gender;
        public String firstName;
        public String lastName;
        public String email;
        public List<Phone> phones;
        public List<TravelerDocument> documents;
    }

    public static class Phone {
        public String deviceType;
        public String countryCallingCode;
        public String number;
    }

    public static class TravelerDocument {
        public String number;
        public String expiryDate;
        public String issuanceCountry;
        public String nationality;
        public String documentType;
        public boolean holder;
    }

    public static class Guest {
        public int tid;
        public String title;
        public String firstName;
        public String lastName;
        public String phone;
        public String email;
    }

    public static class TravelAgent {
        public Contact contact;

        public static class Contact {
            public String email;
        }
    }

    public static class RoomAssociation {
        public List<GuestReference> guestReferences;
        public String hotelOfferId;

        public static class GuestReference {
            public String guestReference;
        }
    }

    public static class Payment {
        public String method;
        public PaymentCard paymentCard;

        public static class PaymentCard {
            public PaymentCardInfo paymentCardInfo;

            public static class PaymentCardInfo {
                public String vendorCode;
                public String cardNumber;
                public String expiryDate;
                public String holderName;
            }
        }
    }

}
