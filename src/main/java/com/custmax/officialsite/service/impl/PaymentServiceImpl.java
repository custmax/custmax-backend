// src/main/java/com/custmax/officialsite/service/impl/PaymentServiceImpl.java
package com.custmax.officialsite.service.impl;

import com.custmax.officialsite.dto.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.PaymentRecordDTO;
import com.custmax.officialsite.entity.PaymentHistory;
import com.custmax.officialsite.payment.PaymentStrategyFactory;
import com.custmax.officialsite.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;

    @Override
    public Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request) {
        return paymentStrategyFactory.getStrategy(request.getPaymentMethod())
                .createPaymentIntent(request);
    }

    @Override
    public Map<String, Object> confirmPayment(ConfirmPaymentRequest request) {
        // 这里假设 paymentMethod 通过 sessionId 或 paymentId 能查到，实际可根据业务调整
        String paymentMethod = "stripe";
        return paymentStrategyFactory.getStrategy(paymentMethod)
                .confirmPayment(request);
    }

    @Override
    public List<PaymentRecordDTO> getCurrentUserPaymentHistory(Long userId) {
        return List.of();
    }

    @Override
    public PaymentHistory getPaymentDetails(String paymentId) {
        return null;
    }
}