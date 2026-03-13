package com.jamuara.crs.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Money {
    private String currency;
    private String amount;
}
