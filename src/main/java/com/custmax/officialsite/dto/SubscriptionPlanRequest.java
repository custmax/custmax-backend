package com.custmax.officialsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlanRequest {
    private Long userId;
    private Long planId;
    private String paymentMethod;
    private Boolean autoRenew;

    // getter 和 setter 省略
}