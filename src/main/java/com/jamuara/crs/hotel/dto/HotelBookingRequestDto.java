package com.jamuara.crs.hotel.dto;

import jakarta.validation.Valid;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Data
public class HotelBookingRequestDto {
        private HotelBookingData data;

    @Data
    public static class HotelBookingData {
        private String type; // "hotel-order"
        private List<Guest> guests;
        private TravelAgent travelAgent;
        private List<RoomAssociation> roomAssociations;
        private Payment payment;
    }

    @Data
    public static class Guest {
        private Long tid;
        private Title title;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;

        public enum Title {
            MR, MRS, MS, DR
        }
    }

    @Data
    public static class TravelAgent {
        private Contact contact;
        @Data
        public static class Contact {
            private String email;
        }
    }

    @Data
    public static class GuestReference {
        private String guestReference;
    }


    @Data
    public static class RoomAssociation {
        private List<GuestReference> guestReferences;
        private String hotelOfferId;
    }


    @Data
    public static class Payment {
        private Method method;
        private PaymentCard paymentCard;

        public enum Method {
            CREDIT_CARD
        }
    }

    @Data
    public static class PaymentCard {
        private PaymentCardInfo paymentCardInfo;
    }

    @Data
    public static class PaymentCardInfo {
        private VendorCode vendorCode;
        private String cardNumber;
        private String expiryDate; // YYYY-MM
        private String holderName;
        public enum VendorCode {
            VI, MC, AX, DC
        }
    }
}
