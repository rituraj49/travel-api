package com.jamuara.crs.flight.repopsitory;

import com.jamuara.crs.model.FlightBooking;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("!nodb")
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
}
