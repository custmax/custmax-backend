package com.custmax.officialsite.dto.subscription;

import lombok.Data;

import java.util.List;

@Data
public class SubscribeToPlanRequest {
    // nullable, for main plan
    private Long planId;
    // for VAS, can be empty or multiple
    private List<Long> valueAddServiceIds;

    private String paymentMethod;
    private Boolean autoRenew;
    private Integer duration;
    private Billing billing;

    public enum Billing {
        MONTHLY, YEARLY
    }
}