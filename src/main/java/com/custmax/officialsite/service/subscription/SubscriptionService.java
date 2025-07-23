package com.custmax.officialsite.service.subscription;

import com.custmax.officialsite.dto.subscription.SubscribeToPlanRequest;
import com.custmax.officialsite.dto.subscription.SubscribeToPlanResponse;

import java.util.Map;

public interface SubscriptionService {
    SubscribeToPlanResponse subscribeToPlan(SubscribeToPlanRequest request);
}
