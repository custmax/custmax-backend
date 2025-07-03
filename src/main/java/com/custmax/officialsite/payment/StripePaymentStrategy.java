// src/main/java/com/custmax/officialsite/payment/StripePaymentStrategy.java
package com.custmax.officialsite.payment;

import com.custmax.officialsite.dto.ConfirmPaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("stripe")
public class StripePaymentStrategy implements PaymentStrategy {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(cancelUrl)
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
                    .putMetadata("subscriptionId", String.valueOf(request.getSubscriptionId()))
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
            Map<String, String> metadata = session.getMetadata();
            Map<String, Object> result = new HashMap<>();
            result.put("status", session.getPaymentStatus());
            result.put("subscriptionId", metadata.get("subscriptionId"));
            result.put("customer", session.getCustomer());
            result.put("amount", session.getAmountTotal());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Stripe confirm payment failed", e);
        }
    }
}