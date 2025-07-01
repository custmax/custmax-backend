// src/main/java/com/custmax/officialsite/payment/StripePaymentStrategy.java
package com.custmax.officialsite.payment;

import com.custmax.officialsite.dto.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.ConfirmPaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("stripe")
public class StripePaymentStrategy implements PaymentStrategy {

    static {
        Stripe.apiKey = "sk_test_xxx"; // 替换为你的 Stripe Secret Key
    }

    @Override
    public Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://www.custmax.com/payment/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("https:///www.custmax.com/payment/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(request.getCurrency())
                                                    .setUnitAmount(request.getAmount().multiply(new java.math.BigDecimal(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(request.getDescription())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            Map<String, Object> result = new HashMap<>();
            result.put("sessionUrl", session.getUrl());
            result.put("sessionId", session.getId());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Stripe create session failed", e);
        }
    }

    @Override
    public Map<String, Object> confirmPayment(ConfirmPaymentRequest request) {
        try {
            Session session = Session.retrieve(request.getSessionId());
            Map<String, Object> result = new HashMap<>();
            result.put("status", session.getPaymentStatus());
            result.put("customer", session.getCustomer());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Stripe confirm payment failed", e);
        }
    }
}