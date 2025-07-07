// src/main/java/com/custmax/officialsite/service/impl/PaymentServiceImpl.java
package com.custmax.officialsite.service.impl;

import com.custmax.officialsite.dto.payment.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.payment.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.payment.PaymentRecordDTO;
import com.custmax.officialsite.entity.payment.PaymentHistory;
import com.custmax.officialsite.entity.subscription.Subscription;
import com.custmax.officialsite.mapper.PaymentHistoryMapper;
import com.custmax.officialsite.mapper.SubscriptionMapper;
import com.custmax.officialsite.payment.PaymentStrategyFactory;
import com.custmax.officialsite.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;
    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Override
    public Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request) {
        return paymentStrategyFactory.getStrategy(request.getPaymentMethod())
                .createPaymentIntent(request);
    }

    @Override
    @Transactional
    public Map<String, Object> confirmPayment(ConfirmPaymentRequest request) {
        // 1. query stripe payment status
        Map<String, Object> stripe = paymentStrategyFactory.getStrategy("stripe").confirmPayment(request);
        Long subscriptionId = Long.valueOf(String.valueOf(stripe.get("subscriptionId")));
        if (stripe.isEmpty()) {
            throw new RuntimeException("Payment confirmation failed");
        }

        // 3. write payment history
        Subscription subscription = subscriptionMapper.selectById(subscriptionId);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUserId(subscription.getUserId());
        paymentHistory.setSubscriptionId(subscription.getId());
        BigDecimal amount = new BigDecimal(String.valueOf(stripe.get("amount")));
        paymentHistory.setAmount(amount.divide(BigDecimal.valueOf(100)));
        paymentHistory.setCurrency("USD");
        paymentHistory.setPaymentMethod("stripe");
        paymentHistory.setPaymentId(request.getSessionId());
        paymentHistory.setStatus("completed");
        paymentHistory.setCreatedAt(new Timestamp(new Date().getTime()));
        paymentHistoryMapper.insert(paymentHistory);

        // 3. activate subscription

        subscription.setStatus(Subscription.Status.active);
        subscriptionMapper.updateById(subscription);

        // 4. return result
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("subscriptionId", subscription.getId());
        return result;
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