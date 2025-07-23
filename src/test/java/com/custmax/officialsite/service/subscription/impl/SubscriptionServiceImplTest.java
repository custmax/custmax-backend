package com.custmax.officialsite.service.subscription.impl;

import com.custmax.officialsite.dto.payment.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.payment.CreatePaymentIntentResponse;
import com.custmax.officialsite.dto.subscription.SubscribeToPlanRequest;
import com.custmax.officialsite.dto.subscription.SubscribeToPlanResponse;
import com.custmax.officialsite.entity.subscription.*;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.mapper.*;
import com.custmax.officialsite.service.payment.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceImplTest {
    @Mock
    private SubscriptionMapper subscriptionMapper;
    @Mock
    private DiscountPolicyMapper discountPolicyMapper;
    @Mock
    private PlanMapper planMapper;
    @Mock
    private ValueAddServicesMapper valueAddServicesMapper;
    @Mock
    private SubscriptionServicesMapper subscriptionServicesMapper;
    @Mock
    private PaymentServiceImpl paymentService;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubscribeToPlan_YearlyWithMultipleVAS() {
        // Arrange
        Long userId = 100L;
        Long planId = 2L; // Premium Plan
        List<Long> vasIds = Arrays.asList(2L, 4L, 5L); // Email, Social, SEO
        int duration = 12;
        BigDecimal planPrice = new BigDecimal("9.99");
        BigDecimal vasPrice = new BigDecimal("9.99");
        BigDecimal annualDiscount = new BigDecimal("0.3");

        // Mock user
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(userId);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Mock plan
        Plan plan = new Plan();
        plan.setId(planId);
        plan.setPrice(planPrice);
        when(planMapper.selectById(planId)).thenReturn(plan);

        // Mock VAS
        ValueAddServices vas = new ValueAddServices();
        vas.setPrice(vasPrice);
        for (Long vasId : vasIds) {
            when(valueAddServicesMapper.selectById(vasId)).thenReturn(vas);
        }

        // Mock discount policy
        SubscriptionDiscountPolicy discountPolicy = new SubscriptionDiscountPolicy();
        discountPolicy.setPolicyKey("annual_discount_rate");
        discountPolicy.setPolicyValue("0.3");
        when(discountPolicyMapper.selectList(any())).thenReturn(Collections.singletonList(discountPolicy));

        // Mock insert/select/update
        doAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setId(123L);
            return null;
        }).when(subscriptionMapper).insert(any(Subscription.class));
        when(subscriptionMapper.selectOne(any())).thenReturn(null);

        // Mock payment
        CreatePaymentIntentResponse paymentResponse = new CreatePaymentIntentResponse();
        when(paymentService.createPaymentIntent(any())).thenReturn(paymentResponse);

        // Build request
        SubscribeToPlanRequest request = new SubscribeToPlanRequest();
        request.setPlanId(planId);
        request.setBilling(SubscribeToPlanRequest.Billing.YEARLY);
        request.setDuration(duration);
        request.setValueAddServiceIds(vasIds);
        request.setAutoRenew(true);
        request.setPaymentMethod("stripe");

        // Act
        SubscribeToPlanResponse response = subscriptionService.subscribeToPlan(request);

        // Assert
        BigDecimal expectedPlanAmount;
        if (duration == 12) {
            expectedPlanAmount = planPrice.multiply(BigDecimal.valueOf(1).subtract(annualDiscount)).multiply(BigDecimal.valueOf(duration));
        } else {
            expectedPlanAmount = planPrice.multiply(BigDecimal.valueOf(duration));
        }

        BigDecimal expectedVasAmount = vasPrice.multiply(BigDecimal.valueOf(12)).multiply(BigDecimal.valueOf(vasIds.size())); // 12 months
        BigDecimal expectedTotal = expectedPlanAmount.add(expectedVasAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedTotal, response.getAmount());
        System.out.println("Successfully subscribed to plan with ID: " + response.getSubscriptionId());
    }
}

