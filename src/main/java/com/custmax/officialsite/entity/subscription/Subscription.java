package com.custmax.officialsite.entity.subscription;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("subscriptions")
public class Subscription {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long planId;
    private Status status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean cancelAtPeriodEnd;
    private String paymentMethod;
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime trialStartDate;
    private LocalDateTime trialEndDate;
    private LocalDateTime renewalDate;
    private Boolean autoRenew;

    public enum Status {
        active, canceled, expired, pending;
    }
}