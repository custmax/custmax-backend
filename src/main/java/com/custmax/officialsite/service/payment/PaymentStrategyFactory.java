// src/main/java/com/custmax/officialsite/payment/PaymentStrategyFactory.java
package com.custmax.officialsite.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStrategyFactory {
    @Autowired
    private Map<String, PaymentStrategy> strategyMap;

    public PaymentStrategy getStrategy(String paymentMethod) {
        return strategyMap.get(paymentMethod);
    }
}