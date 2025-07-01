package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.CreatePaymentIntentRequest;
import com.custmax.officialsite.service.PaymentService;
import com.custmax.officialsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * Create Stripe payment intent for subscription or service.
     */
    @PostMapping("/create-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody CreatePaymentIntentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentIntent(request));
    }

    /**
     * Confirm successful payment and activate subscription.
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        var paymentResult = paymentService.confirmPayment(request);
//        if ("paid".equals(paymentResult.get("status"))) {
//            var planRequest = request.toSubscriptionPlanRequest();
//            var subResult = subscriptionService.subscribeToPlan(planRequest);
//            paymentResult.putAll(subResult);
//        }
        return ResponseEntity.ok(paymentResult);
    }

    /**
     * Get details of a specific payment transaction.
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(String.valueOf(paymentId)));
    }
}