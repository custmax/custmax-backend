package com.custmax.officialsite.dto.payment;

import lombok.Data;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-10
 * @Description:
 * @Version: 1.0
 */
@Data
public class ConfirmPaymentInfoDTO {
    Long amount;
    Long subscriptionId;
    String status;
    String customer;
}
