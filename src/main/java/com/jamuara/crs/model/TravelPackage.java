package com.jamuara.crs.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class TravelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String packageId;

    private String startDate;
    private String endDate;

    @ManyToOne()
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "travelPackage")
    private List<Reservation> flightReservation;

    @OneToMany(mappedBy = "travelPackage")
    private List<HotelReservation> hotelReservation;

    @PrePersist
    public void generatePackageId() {
        if (packageId == null) {
            packageId = UUID.randomUUID().toString();
        }
    }
}
