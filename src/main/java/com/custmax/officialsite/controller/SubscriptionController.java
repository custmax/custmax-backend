package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.SubscriptionDTO;
import com.custmax.officialsite.dto.SubscriptionPlanRequest;
import com.custmax.officialsite.dto.SubscriptionServiceRequest;
import com.custmax.officialsite.dto.UpdateSubscriptionRequest;
import com.custmax.officialsite.service.impl.SubscriptionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    /**
     * *Subscribe user to a specific plan (Free/Paid/Enterprise)*
     * @param request
     * @return
     */
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
    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Map<String, Object>> cancelSubscription(@PathVariable String subscriptionId) {
        Map<String, Object> response = subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/subscription")
    public List<SubscriptionDTO> getUserSubscriptions() {
        return subscriptionService.getCurrentUserSubscriptions();
    }


}