// src/main/java/com/custmax/officialsite/service/impl/PaymentServiceImpl.java
package com.custmax.officialsite.service.payment.impl;

import com.custmax.officialsite.dto.payment.*;
import com.custmax.officialsite.entity.payment.PaymentHistory;
import com.custmax.officialsite.entity.subscription.Subscription;
import com.custmax.officialsite.mapper.PaymentHistoryMapper;
import com.custmax.officialsite.mapper.SubscriptionMapper;
import com.custmax.officialsite.service.payment.PaymentStrategyFactory;
import com.custmax.officialsite.service.payment.PaymentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;
    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Override
    public CreatePaymentIntentResponse createPaymentIntent(CreatePaymentIntentRequest request) {
        CreatePaymentIntentResponse response = new CreatePaymentIntentResponse();
        CreatePaymentIntentInfoDTO intentInfoDTO = (CreatePaymentIntentInfoDTO)paymentStrategyFactory.getStrategy(request.getPaymentMethod())
                .createPaymentIntent(request);
        BeanUtils.copyProperties(intentInfoDTO,response);
        return response;
    }

    @Override
    @Transactional
    public ConfirmPaymentResponse confirmPayment(String sessionId) {
        // 1. query stripe payment status
        ConfirmPaymentInfoDTO confirmPaymentInfoDTO = (ConfirmPaymentInfoDTO) paymentStrategyFactory.
                getStrategy("stripe").
                confirmPayment(sessionId);
        Long subscriptionId = confirmPaymentInfoDTO.getSubscriptionId();

        // 3. write payment history
        Subscription subscription = subscriptionMapper.selectById(subscriptionId);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUserId(subscription.getUserId());
        paymentHistory.setSubscriptionId(subscription.getId());
        BigDecimal amount = new BigDecimal(String.valueOf(confirmPaymentInfoDTO.getAmount()));
        paymentHistory.setAmount(amount.divide(BigDecimal.valueOf(100)));
        paymentHistory.setCurrency("USD");
        paymentHistory.setPaymentMethod("stripe");
        paymentHistory.setPaymentId(sessionId);
        paymentHistory.setStatus(PaymentHistory.PaymentStatus.COMPLETED);
        paymentHistory.setCreatedAt(new Timestamp(new Date().getTime()));
        paymentHistoryMapper.insert(paymentHistory);

        // 3. activate subscription

        subscription.setStatus(Subscription.Status.active);
        subscriptionMapper.updateById(subscription);

        // 4. return result
        ConfirmPaymentResponse response = new ConfirmPaymentResponse();
        response.setStatus(ConfirmPaymentResponse.Status.SUCCESS);
        response.setSubscriptionId(confirmPaymentInfoDTO.getSubscriptionId());
        return response;
    }

    @Override
    public List<GetPaymentDetailsResponse> getCurrentUserPaymentHistory(Long userId) {
        return List.of();
    }

}