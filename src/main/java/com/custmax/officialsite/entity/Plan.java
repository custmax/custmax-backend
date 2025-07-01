package com.custmax.officialsite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("plans")
public class Plan {
    @TableId
    private Long id;
    private String name;
    private BigDecimal price;
    private String billingPeriod;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer trialPeriodDays;
    private String features;
}