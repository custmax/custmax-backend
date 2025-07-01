package com.custmax.officialsite.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("payment_history")
public class PaymentHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private String subscriptionItemId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String paymentId;
    private String invoiceId;
    private String status;
    private Timestamp createdAt;
}