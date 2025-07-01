package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.CreatePaymentIntentRequest;
import com.custmax.officialsite.service.PaymentService;
import com.custmax.officialsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    /**
     * Confirm successful payment and activate subscription.
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        return paymentService.confirmPayment(request);
    }

    /**
     * Get details of a specific payment transaction.
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(String.valueOf(paymentId)));
    }
}