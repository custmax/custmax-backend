package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.payment.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.payment.ConfirmPaymentResponse;
import com.custmax.officialsite.dto.payment.GetPaymentDetailsResponse;
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
     * @param sessionId the ID of the payment session
     * @return ResponseEntity with payment initiation status
     */
    @Operation(summary = "Initiate a payment transaction")
    @PostMapping("/confirm")
    public ResponseEntity<ConfirmPaymentResponse> confirmPayment(@RequestParam String sessionId) {
        ConfirmPaymentResponse result = paymentService.confirmPayment(sessionId);
        return ResponseEntity.ok(result);
    }

}