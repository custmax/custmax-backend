package com.custmax.officialsite.dto.subscription;

import com.custmax.officialsite.dto.payment.CreatePaymentIntentResponse;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-10
 * @Description:
 * @Version: 1.0
 */
@Data
public class SubscribeToPlanResponse {
        public enum Status {
            SUCCESS,
            FAILED
        }
        private Status status;
        Long subscriptionId;
        BigDecimal amount;
        CreatePaymentIntentResponse paymentIntent;
}
