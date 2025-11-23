package com.jamuara.crs.common.repository;

import com.jamuara.crs.model.Payment;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Profile("!nodb")
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTxnid(@Param("txnid") String txnid);
}
