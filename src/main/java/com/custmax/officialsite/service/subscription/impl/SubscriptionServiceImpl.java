package com.custmax.officialsite.service.subscription.impl;

import com.custmax.officialsite.dto.payment.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.payment.CreatePaymentIntentResponse;
import com.custmax.officialsite.dto.subscription.SubscribeToPlanResponse;
import com.custmax.officialsite.dto.subscription.SubscribeToPlanRequest;
import com.custmax.officialsite.entity.subscription.*;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.mapper.*;
import com.custmax.officialsite.service.payment.impl.PaymentServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custmax.officialsite.service.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private ValueAddServicesMapper valueAddServicesMapper;
    @Autowired
    private SubscriptionServicesMapper subscriptionServicesMapper;
    @Autowired
    private PaymentServiceImpl paymentService;

    @Override
    @Transactional
    public SubscribeToPlanResponse subscribeToPlan(SubscribeToPlanRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        // 1. Find user's active subscription
        LambdaQueryWrapper<Subscription> query = new LambdaQueryWrapper<>();
        query.eq(Subscription::getUserId, userId)
                .in(Subscription::getStatus, "active", "pending")
                .gt(Subscription::getEndDate, LocalDateTime.now());
        Subscription existingSubscription = subscriptionMapper.selectOne(query);

        // Verify user has an active subscription if planId is null
        if (request.getPlanId() == null && existingSubscription == null) {
            throw new IllegalArgumentException("User must have an active subscription to add value-added services");
        }

        BigDecimal finalAmount = BigDecimal.ZERO;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate;

        Subscription subscription;

        // 2. Handle main plan subscription if requested
        if (request.getPlanId() != null) {
            // Get plan info
            Plan plan = planMapper.selectById(request.getPlanId());
            if (plan == null) {
                throw new IllegalArgumentException("Invalid plan ID");
            }

            // Calculate subscription duration and end date
            int duration = request.getDuration() != null ? request.getDuration() : 1;
            SubscribeToPlanRequest.Billing billing = request.getBilling();

            // Calculate end date
            endDate = startDate;
            if (billing == SubscribeToPlanRequest.Billing.YEARLY) {
                endDate = endDate.plusMonths(duration);
            } else {
                endDate = endDate.plusMonths(duration);
            }

            // Calculate price with discount
            BigDecimal planPrice = calculatePlanPrice(plan.getPrice(), billing, duration);
            finalAmount = finalAmount.add(planPrice);

            // Create or update subscription
            subscription = new Subscription();
            subscription.setUserId(userId);
            subscription.setPlanId(request.getPlanId());
            subscription.setStatus(Subscription.Status.pending);
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            subscription.setAutoRenew(Boolean.TRUE.equals(request.getAutoRenew()));
            subscription.setPaymentMethod(request.getPaymentMethod());
            subscription.setCancelAtPeriodEnd(true);
            subscriptionMapper.insert(subscription);
        } else {
            // Use existing subscription details when only adding VAS
            subscription = existingSubscription;
            endDate = subscription.getEndDate();
        }

        // 3. Handle value-added services if requested
        if (request.getValueAddServiceIds() != null && !request.getValueAddServiceIds().isEmpty()) {
            // Get VAS pricing
            for (Long serviceId : request.getValueAddServiceIds()) {
                ValueAddServices service = valueAddServicesMapper.selectById(serviceId);
                if (service == null) {
                    throw new IllegalArgumentException("Invalid value-added service ID: " + serviceId);
                }

                // Calculate prorated price based on remaining subscription duration
                BigDecimal vasPrice = BigDecimal.valueOf(12).multiply(service.getPrice());
                finalAmount = finalAmount.add(vasPrice);

                // Create subscription service record
                SubscriptionServices subscriptionService = SubscriptionServices.builder()
                        .subscriptionId(subscription.getId())
                        .serviceId(serviceId)
                        .build();
                subscriptionServicesMapper.insert(subscriptionService);
            }
        }
        finalAmount = finalAmount.setScale(2, RoundingMode.HALF_UP);
        // 4. Process payment
        CreatePaymentIntentRequest paymentRequest = new CreatePaymentIntentRequest();
        paymentRequest.setAmount(finalAmount);
        paymentRequest.setPaymentMethod(request.getPaymentMethod());
        paymentRequest.setSubscriptionId(subscription.getId());
        paymentRequest.setCurrency("usd");
        paymentRequest.setDescription(request.getPlanId() != null ?
                planMapper.selectById(request.getPlanId()).getName() : "Value Added Services");
        CreatePaymentIntentResponse paymentIntentResponse = paymentService.createPaymentIntent(paymentRequest);

        // 5. Update subscription with payment info
        subscriptionMapper.updateById(subscription);

        // 6. Return result
        SubscribeToPlanResponse response = new SubscribeToPlanResponse();
        response.setStatus(SubscribeToPlanResponse.Status.SUCCESS);
        response.setSubscriptionId(subscription.getId());
        response.setPaymentIntent(paymentIntentResponse);
        response.setAmount(finalAmount);
        return response;
    }

    // Helper method to calculate plan price with discounts
    private BigDecimal calculatePlanPrice(BigDecimal basePrice, SubscribeToPlanRequest.Billing billing, int duration) {
        // Get discount policies
        List<SubscriptionDiscountPolicy> policies = discountPolicyMapper.selectList(null);
        Map<String, SubscriptionDiscountPolicy> policyMap = policies.stream()
                .collect(Collectors.toMap(SubscriptionDiscountPolicy::getPolicyKey, p -> p));

        BigDecimal finalPrice = basePrice;

        if (billing == SubscribeToPlanRequest.Billing.YEARLY) {
            BigDecimal discount = new BigDecimal(policyMap.get("annual_discount_rate").getPolicyValue());
            finalPrice = basePrice.multiply(BigDecimal.ONE.subtract(discount)).multiply(BigDecimal.valueOf(duration));
        } else if (billing == SubscribeToPlanRequest.Billing.MONTHLY) {
            finalPrice = basePrice.multiply(BigDecimal.valueOf(duration));
        }

        return finalPrice;
    }

}
