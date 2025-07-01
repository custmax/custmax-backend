package com.custmax.officialsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentIntentRequest {
    private Long userId;
    private Long subscriptionId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod; // 如 stripe、alipay 等
    private String description;
}