package com.jamuara.crs.admin.priceChanges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceRuleRepository extends JpaRepository<PriceRule, Long> {

    @Query("""
        SELECT r FROM PriceRule r
        WHERE r.tripType = :tripType
        AND r.active = true
        AND :today BETWEEN r.startDate AND r.endDate
    """)
    List<PriceRule> findActiveRules(PriceRule.TripType tripType, LocalDate today);

   // Optional<PriceRule> findActiveRuleByTripType(PriceRule.TripType tripType);

}
