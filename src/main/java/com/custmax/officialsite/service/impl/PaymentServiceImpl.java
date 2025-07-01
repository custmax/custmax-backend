// src/main/java/com/custmax/officialsite/service/impl/PaymentServiceImpl.java
package com.custmax.officialsite.service.impl;

import com.custmax.officialsite.dto.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.ConfirmPaymentRequest;
import com.custmax.officialsite.dto.PaymentRecordDTO;
import com.custmax.officialsite.entity.PaymentHistory;
import com.custmax.officialsite.mapper.PaymentHistoryMapper;
import com.custmax.officialsite.payment.PaymentStrategyFactory;
import com.custmax.officialsite.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;
    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;

    @Override
    public Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request) {
        return paymentStrategyFactory.getStrategy(request.getPaymentMethod())
                .createPaymentIntent(request);
    }

    @Transactional
    public Map<String, Object> confirmPayment(ConfirmPaymentRequest request) {
        // 1. 查询 Stripe 支付状态
        boolean paid = stripeService.checkPaymentStatus(request.getSessionId());
        if (!paid) {
            throw new RuntimeException("支付未完成");
        }

        // 2. 激活订阅
        Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId());
        subscription.setStatus("active");
        subscription.setStartDate(new Date());
        // 这里可以根据业务设置 endDate、renewalDate 等
        subscription.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        subscriptionRepository.save(subscription);

        // 3. 写入支付历史
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUserId(subscription.getUserId());
        paymentHistory.setSubscriptionId(subscription.getId());
        paymentHistory.setAmount(request.getAmount());
        paymentHistory.setCurrency("USD");
        paymentHistory.setPaymentMethod("stripe");
        paymentHistory.setPaymentId(request.getSessionId());
        paymentHistory.setStatus("completed");
        paymentHistory.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        paymentHistoryRepository.save(paymentHistory);

        // 4. 返回结果
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