package com.custmax.officialsite.service;

import java.util.List;
import java.util.Map;

public interface SubscriptionService {
    Map<String, Object> updateSubscription(String subscriptionId, UpdateSubscriptionRequest request);

    Map<String, Object> subscribeToService(SubscriptionServiceRequest request);

    Map<String, Object> subscribeToPlan(SubscriptionPlanRequest request);

    Map<String, Object> cancelSubscription(String subscriptionId);

    List<SubscriptionDTO> getCurrentUserSubscriptions();
}
