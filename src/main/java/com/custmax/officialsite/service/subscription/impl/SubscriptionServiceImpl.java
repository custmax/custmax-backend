package com.custmax.officialsite.service.subscription.impl;

import com.custmax.officialsite.dto.payment.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.subscription.SubscriptionResponse;
import com.custmax.officialsite.dto.subscription.SubscriptionPlanRequest;
import com.custmax.officialsite.dto.subscription.SubscriptionServiceRequest;
import com.custmax.officialsite.dto.subscription.UpdateSubscriptionRequest;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.entity.subscription.SubscriptionDiscountPolicy;
import com.custmax.officialsite.mapper.DiscountPolicyMapper;
import com.custmax.officialsite.service.payment.impl.PaymentServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custmax.officialsite.entity.subscription.Subscription;
import com.custmax.officialsite.mapper.PlanMapper;
import com.custmax.officialsite.mapper.SubscriptionMapper;
import com.custmax.officialsite.service.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.custmax.officialsite.entity.subscription.Plan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private DiscountPolicyMapper discountPolicyMapper;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 1. Check if user already has an active or pending subscription
        LambdaQueryWrapper<Subscription> query = new LambdaQueryWrapper<>();
        query.eq(Subscription::getUserId, userDetails.getUserId())
                .in(Subscription::getStatus, "active", "pending")
                .gt(Subscription::getEndDate, LocalDateTime.now());
        Subscription existing = subscriptionMapper.selectOne(query);
        boolean isRenewal = existing != null;

        // 2. Get plan info
        LambdaQueryWrapper<Plan> planQuery = new LambdaQueryWrapper<>();
        planQuery.eq(Plan::getId, request.getPlanId());
        Plan plan = planMapper.selectOne(planQuery);

        // 3. Get discount policies
        List<SubscriptionDiscountPolicy> policies = discountPolicyMapper.selectList(null);
        Map<String, SubscriptionDiscountPolicy> policyMap = policies.stream()
                .collect(Collectors.toMap(SubscriptionDiscountPolicy::getPolicyKey, p -> p));

        // 4. Calculate final amount
        BigDecimal price = plan.getPrice();
        BigDecimal finalAmount = price;
        int duration = request.getDuration() != null ? request.getDuration() : 1;
        String planType = request.getPlanType();

        if ("ANNUAL".equalsIgnoreCase(planType)) {
            BigDecimal discount = new BigDecimal(policyMap.get("annual_discount_rate").getPolicyValue());
            finalAmount = price.multiply(BigDecimal.ONE.subtract(discount)).multiply(BigDecimal.valueOf(duration));
        } else if ("MONTHLY".equalsIgnoreCase(planType)) {
            int firstMonths = Integer.parseInt(policyMap.get("monthly_first3_months").getPolicyValue());
            BigDecimal firstMonthPrice = new BigDecimal(policyMap.get("monthly_first3_price").getPolicyValue());
            if (!isRenewal && duration > 0) {
                int discountMonths = Math.min(duration, firstMonths);
                int normalMonths = duration - discountMonths;
                finalAmount = firstMonthPrice.multiply(BigDecimal.valueOf(discountMonths))
                        .add(price.multiply(BigDecimal.valueOf(normalMonths)));
            } else {
                finalAmount = price.multiply(BigDecimal.valueOf(duration));
            }
        }

        // 5. Set end date
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate;
        if ("ANNUAL".equalsIgnoreCase(planType)) {
            endDate = endDate.plusMonths(duration);
        } else if ("MONTHLY".equalsIgnoreCase(planType)) {
            endDate = endDate.plusMonths(duration);
        }

        // 6. Create subscription
        Subscription subscription = new Subscription();
        subscription.setUserId(userDetails.getUserId());
        subscription.setPlanId(request.getPlanId());
        subscription.setStatus(Subscription.Status.pending);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setAutoRenew(Boolean.TRUE.equals(request.getAutoRenew()));
        subscription.setPaymentMethod(request.getPaymentMethod());
        subscription.setCancelAtPeriodEnd(true);
        subscriptionMapper.insert(subscription);

        // 7. Create payment intent
        CreatePaymentIntentRequest paymentRequest = new CreatePaymentIntentRequest();
        paymentRequest.setAmount(finalAmount);
        paymentRequest.setPaymentMethod(request.getPaymentMethod());
        paymentRequest.setSubscriptionId(subscription.getId());
        paymentRequest.setCurrency("usd");
        paymentRequest.setDescription(plan.getName());
        Map<String, Object> paymentResult = paymentService.createPaymentIntent(paymentRequest);

        String paymentId = (String) paymentResult.get("paymentId");
        subscription.setPaymentId(paymentId);
        subscriptionMapper.updateById(subscription);

        // 8. Return result
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

    @Override
    public List<SubscriptionResponse> getCurrentUserSubscriptions(CustomUserDetails user) {
        LambdaQueryWrapper<Subscription> query = new LambdaQueryWrapper<>();
        query.eq(Subscription::getUserId, user.getUserId())
                .orderByDesc(Subscription::getStartDate);
        List<Subscription> subscriptions = subscriptionMapper.selectList(query);

        return subscriptions.stream().map(subscription -> {
            SubscriptionResponse response = new SubscriptionResponse();
            Plan plan = planMapper.selectById(subscription.getPlanId());
            response.setStatus(subscription.getStatus());
            response.setPlanName(plan.getName());
            response.setPlanDescription(plan.getDescription());
            response.setStartTime(subscription.getStartDate());
            response.setEndTime(subscription.getEndDate());
            return response;
        }).collect(Collectors.toList());
    }
}
