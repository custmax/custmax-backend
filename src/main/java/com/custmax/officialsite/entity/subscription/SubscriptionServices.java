package com.custmax.officialsite.entity.subscription;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-11
 * @Description:
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("subscription_services")
public class SubscriptionServices {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("subscription_id")
    private Long subscriptionId;

    @TableField("service_id")
    private Long serviceId;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}