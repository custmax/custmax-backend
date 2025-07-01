package com.custmax.officialsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custmax.officialsite.dto.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.SubscriptionPlanRequest;
import com.custmax.officialsite.dto.SubscriptionServiceRequest;
import com.custmax.officialsite.dto.UpdateSubscriptionRequest;
import com.custmax.officialsite.entity.Subscription;
import com.custmax.officialsite.mapper.PlanMapper;
import com.custmax.officialsite.mapper.SubscriptionMapper;
import com.custmax.officialsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.custmax.officialsite.entity.Plan;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Override
    public Map<String, Object> updateSubscription(String subscriptionId, UpdateSubscriptionRequest request) {
        return Map.of();
    }

    @Override
    public Map<String, Object> subscribeToService(SubscriptionServiceRequest request) {
        return Map.of();
    }

    @Override
    public Map<String, Object> subscribeToPlan(SubscriptionPlanRequest request) {
        Subscription subscription = new Subscription();
        subscription.setUserId(request.getUserId());
        subscription.setPlanId(request.getPlanId());
        subscription.setStatus("pending");
        subscription.setStartDate(LocalDateTime.now());
        subscription.setAutoRenew(Boolean.TRUE.equals(request.getAutoRenew()));
        subscription.setPaymentMethod(request.getPaymentMethod());
        subscription.setCancelAtPeriodEnd(false);
        subscriptionMapper.insert(subscription);

        CreatePaymentIntentRequest paymentRequest = new CreatePaymentIntentRequest();
        LambdaQueryWrapper<Plan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Plan::getId, request.getPlanId());
        Plan plan = planMapper.selectOne(lambdaQueryWrapper);
        paymentRequest.setAmount(plan.getPrice());
        paymentRequest.setPaymentMethod(request.getPaymentMethod());
        paymentRequest.setSubscriptionId(subscription.getId());
        paymentRequest.setCurrency("usd");
        paymentRequest.setDescription(plan.getName());
        Map<String, Object> paymentResult = paymentService.createPaymentIntent(paymentRequest);

        String paymentId = (String) paymentResult.get("paymentId");
        subscription.setPaymentId(paymentId);
        subscriptionMapper.updateById(subscription);

        // 4. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("subscriptionId", subscription.getId());
        result.put("paymentIntent", paymentResult);
        return result;
    }

    @Override
    public Map<String, Object> cancelSubscription(String subscriptionId) {
        return Map.of();
    }
}
