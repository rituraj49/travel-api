package com.jamuara.crs.admin.priceChanges.repository;

import com.jamuara.crs.admin.priceChanges.model.PriceRule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PriceRuleRepository extends JpaRepository<PriceRule, Long> {

    @Query("SELECT r FROM PriceRule r WHERE r.active = true AND :date BETWEEN r.startDate AND r.endDate")
    List<PriceRule> findActiveRules(@Param("date") LocalDate date);


}
