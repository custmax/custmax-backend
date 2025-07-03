package com.custmax.officialsite.dto.payment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentRecordDTO {
    private Long id;
    private String paymentMethod;
    private String status;
    private Long amount;
    private LocalDateTime createdAt;
    private String description;
}

