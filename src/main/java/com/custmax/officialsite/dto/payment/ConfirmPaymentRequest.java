package com.custmax.officialsite.dto.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfirmPaymentRequest {
    private String sessionId;
}

