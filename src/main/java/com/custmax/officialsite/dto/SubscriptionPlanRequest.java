package com.custmax.officialsite.dto;

import lombok.Data;

@Data
public class SubscriptionPlanRequest {
    private Long planId;
    private String paymentMethod;
    private Boolean autoRenew;
    private Integer duration;
    private String planType;
}