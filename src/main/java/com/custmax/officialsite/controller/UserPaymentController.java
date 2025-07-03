package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.payment.PaymentRecordDTO;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.service.PaymentService;
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
public class UserPaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Retrieve user's payment transaction history.
     */
    @GetMapping
    public ResponseEntity<PaymentRecordDTO> getMyPaymentHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        PaymentRecordDTO response = (PaymentRecordDTO) paymentService.getCurrentUserPaymentHistory(userId);
        return ResponseEntity.ok(response);
    }
}