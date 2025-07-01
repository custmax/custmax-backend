package com.custmax.officialsite.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.custmax.officialsite.dto.SubscriptionPlanRequest;
import com.custmax.officialsite.dto.SubscriptionServiceRequest;
import com.custmax.officialsite.dto.UpdateSubscriptionRequest;
import com.custmax.officialsite.entity.Subscription;
import com.custmax.officialsite.mapper.SubscriptionMapper;
import com.custmax.officialsite.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

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
        subscription.setStatus("active");
        subscription.setStartDate(LocalDateTime.now());
        subscription.setAutoRenew(Boolean.TRUE.equals(request.getAutoRenew()));
        subscription.setPaymentMethod(request.getPaymentMethod());
        subscription.setCancelAtPeriodEnd(false);

        subscriptionMapper.insert(subscription);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("subscriptionId", subscription.getId());
        return result;
    }

    @Override
    public Map<String, Object> cancelSubscription(String subscriptionId) {
        return Map.of();
    }
}
