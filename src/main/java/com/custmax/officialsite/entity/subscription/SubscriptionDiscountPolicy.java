package com.custmax.officialsite.entity.subscription;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("subscription_discount_policy")
public class SubscriptionDiscountPolicy {
    private Long id;
    private String policyKey;
    private String policyValue;
    private String description;
    private LocalDateTime updatedAt;
}