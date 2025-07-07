package com.custmax.officialsite.dto.subscription;

import com.custmax.officialsite.entity.subscription.Subscription;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionResponse {
    Subscription.Status status;
    String planName;
    String planDescription;
    LocalDateTime startTime;
    LocalDateTime endTime;
}

