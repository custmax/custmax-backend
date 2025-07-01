package com.custmax.officialsite.entity;

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
    private String status;
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
}