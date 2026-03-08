package com.jamuara.crs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jamuara.crs.profile.dto.UserProfileDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
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

    @Getter
    @Setter
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String country;

        @Column(name = "zip_code")
        private String zipCode;
    }

    public UserProfile(String kcUserId) {
        this.kcUserId = kcUserId;
    }
}

