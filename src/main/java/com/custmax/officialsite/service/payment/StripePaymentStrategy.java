// src/main/java/com/custmax/officialsite/payment/StripePaymentStrategy.java
package com.custmax.officialsite.service.payment;

import com.custmax.officialsite.dto.payment.ConfirmPaymentInfoDTO;
import com.custmax.officialsite.dto.payment.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.payment.CreatePaymentIntentInfoDTO;
import com.custmax.officialsite.dto.payment.CreatePaymentIntentRequest;
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
    public CreatePaymentIntentInfoDTO createPaymentIntent(CreatePaymentIntentRequest request) {
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

            CreatePaymentIntentInfoDTO info = new CreatePaymentIntentInfoDTO();
            info.setSessionId(session.getId());
            info.setSessionUrl(session.getUrl());
            return info;
        } catch (Exception e) {
            throw new RuntimeException("Stripe create session failed", e);
        }
    }

    @Override
    public ConfirmPaymentInfoDTO confirmPayment(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            Map<String, String> metadata = session.getMetadata();
            ConfirmPaymentInfoDTO info = new ConfirmPaymentInfoDTO();

            info.setStatus(session.getPaymentStatus());
            info.setSubscriptionId(Long.valueOf(metadata.get("subscriptionId")));
            info.setCustomer(metadata.get("customer"));
            info.setAmount(session.getAmountTotal());

            return info;
        } catch (Exception e) {
            throw new RuntimeException("Stripe confirm payment failed", e);
        }
    }
}