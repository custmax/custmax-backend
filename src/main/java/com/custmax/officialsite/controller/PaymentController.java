package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.payment.ConfirmPaymentRequest;
import com.custmax.officialsite.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment Management", description = "Manage payment transactions and subscriptions")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Initiates a payment transaction.
     * @param request contains payment details
     * @return ResponseEntity with payment initiation status
     */
    @Operation(summary = "Initiate a payment transaction")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        Object result = paymentService.confirmPayment(request);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves payment details by ID.
     * @param paymentId the ID of the payment
     * @return ResponseEntity with payment details
     */
    @Operation(summary = "Get payment details by ID")
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(String.valueOf(paymentId)));
    }
}