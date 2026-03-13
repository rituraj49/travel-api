package com.jamuara.crs.currency.service;

import com.jamuara.crs.currency.dto.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyService {
    public Money exchangeCurrency(String from, String to, String amount) {
        BigDecimal sourceAmount = new BigDecimal(amount);

        BigDecimal rate = switch (from + "_" + to) {
            case "EUR_INR" -> new BigDecimal("90");
            case "USD_INR" -> new BigDecimal("83");
            default -> BigDecimal.TEN;
        };

        BigDecimal convertedAmount = sourceAmount.multiply(rate);

        return new Money(to, convertedAmount.toString());
    }
}
