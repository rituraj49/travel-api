package com.jamuara.crs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jamuara.crs.profile.dto.UserProfileDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String kcUserId;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @Data
    @Embeddable
    public static class Address {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String country;
        private String zipCode;
    }

    public UserProfile(String kcUserId) {
        this.kcUserId = kcUserId;
    }
}
