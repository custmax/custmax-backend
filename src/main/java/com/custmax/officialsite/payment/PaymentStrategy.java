// src/main/java/com/custmax/officialsite/payment/PaymentStrategy.java
package com.custmax.officialsite.payment;

import com.custmax.officialsite.dto.ConfirmPaymentRequest;

import java.util.Map;

public interface PaymentStrategy {
    Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request);
    Map<String, Object> confirmPayment(ConfirmPaymentRequest request);
}