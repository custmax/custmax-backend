// PaymentRecordDTO.java
package com.custmax.officialsite.dto;

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