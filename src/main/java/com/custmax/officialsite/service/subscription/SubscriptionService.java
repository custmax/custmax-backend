package com.custmax.officialsite.service.subscription;

import com.custmax.officialsite.dto.subscription.SubscriptionResponse;
import com.custmax.officialsite.dto.subscription.SubscriptionPlanRequest;
import com.custmax.officialsite.dto.subscription.SubscriptionServiceRequest;
import com.custmax.officialsite.dto.subscription.UpdateSubscriptionRequest;
import com.custmax.officialsite.entity.user.CustomUserDetails;

import java.util.List;
import java.util.Map;

public interface SubscriptionService {
    Map<String, Object> updateSubscription(String subscriptionId, UpdateSubscriptionRequest request);

    Map<String, Object> subscribeToService(SubscriptionServiceRequest request);

    Map<String, Object> subscribeToPlan(SubscriptionPlanRequest request);

    Map<String, Object> cancelSubscription(String subscriptionId);

    List<SubscriptionResponse> getCurrentUserSubscriptions(CustomUserDetails user);
}
