package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.subscription.SubscribeToPlanRequest;
import com.custmax.officialsite.dto.subscription.SubscribeToPlanResponse;
import com.custmax.officialsite.service.subscription.impl.SubscriptionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

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
    public ResponseEntity<SubscribeToPlanResponse> subscribeToPlan(@RequestBody SubscribeToPlanRequest request) {
        SubscribeToPlanResponse response = subscriptionService.subscribeToPlan(request);
        return ResponseEntity.ok(response);
    }
}