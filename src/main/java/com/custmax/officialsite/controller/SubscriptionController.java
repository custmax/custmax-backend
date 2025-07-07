package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.subscription.SubscriptionResponse;
import com.custmax.officialsite.dto.subscription.SubscriptionPlanRequest;
import com.custmax.officialsite.dto.subscription.SubscriptionServiceRequest;
import com.custmax.officialsite.dto.subscription.UpdateSubscriptionRequest;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.service.impl.SubscriptionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Subscription Management", description = "Manage user subscriptions to plans and services")
public class SubscriptionController {

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    /**
     * *Subscribe user to a specific plan (Free/Paid/Enterprise)*
     * @param request
     * @return
     */

    @Operation(summary = "Subscribe user to a specific plan (Free/Paid/Enterprise)")
    @PostMapping("/subscriptions/plan")
    public ResponseEntity<Map<String, Object>> subscribeToPlan(@RequestBody SubscriptionPlanRequest request) {
        Map<String, Object> response = subscriptionService.subscribeToPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Subscribe user to a value-added service*
     * @param request 包含服务订阅信息的请求体
     * @return 包含订阅结果的响应体
     */
    @Operation(summary = "Subscribe user to a value-added service")
    @PostMapping("/subscriptions/service")
    public ResponseEntity<Map<String, Object>> subscribeToService(@RequestBody SubscriptionServiceRequest request) {
        Map<String, Object> response = subscriptionService.subscribeToService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Upgrade/downgrade subscription plan or modify services*
     * @param subscriptionId
     * @param request
     * @return
     */
    @Operation(summary = "Upgrade/downgrade subscription plan or modify services")
    @PutMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Map<String, Object>> updateSubscription(
            @PathVariable String subscriptionId,
            @RequestBody UpdateSubscriptionRequest request) {
        Map<String, Object> response = subscriptionService.updateSubscription(subscriptionId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Cancel a specific subscription or service
     * @param subscriptionId
     * @return
     */
    @Operation(summary = "Cancel a specific subscription or service")
    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Map<String, Object>> cancelSubscription(@PathVariable String subscriptionId) {
        Map<String, Object> response = subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all subscriptions for the current user
     * @return List of SubscriptionDTO objects representing the user's subscriptions
     */
    @Operation(summary = "Get all subscriptions for the current user")
    @GetMapping("/me/subscription")
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(subscriptionService.getCurrentUserSubscriptions(user));
    }


}