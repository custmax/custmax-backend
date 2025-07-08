package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.payment.PaymentRecordDTO;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User payment history controller.
 */
@RestController
@RequestMapping("/api/users/me/payments")
@Tag(name = "User Payment Management", description = "Manage user payment history and transactions")
public class UserPaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Retrieves the current user's payment history.
     * @return ResponseEntity containing the user's payment history.
     */
    @Operation(summary = "Get current user's payment history")
    @GetMapping
    public ResponseEntity<PaymentRecordDTO> getMyPaymentHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        PaymentRecordDTO response = (PaymentRecordDTO) paymentService.getCurrentUserPaymentHistory(userId);
        return ResponseEntity.ok(response);
    }
}