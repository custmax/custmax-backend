package com.custmax.officialsite.entity.payment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory {
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    private Long id;
    private Long userId;
    private Long subscriptionId;
    private String subscriptionItemId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String paymentId;
    private String invoiceId;
    private PaymentStatus status;
    private Timestamp createdAt;
}