package com.custmax.officialsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentIntentRequest {
    private Long userId;
    private Long subscriptionId;
    private BigDecimal amount;
    private String currency;
    // stripe、paypal...
    private String paymentMethod;
    private String description;
}