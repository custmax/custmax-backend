package com.custmax.officialsite.service.email.impl;

import com.custmax.officialsite.service.email.EmailSenderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * email sender strategy factory
 * This factory is responsible for managing different email sender strategies.
 *
 * @author Vincent
 */
@Component
public class EmailSenderStrategyFactory {
    @Autowired
    private Map<String, EmailSenderStrategy> strategies;

    /**
     * fetch the email sender strategy by name
     *
     * @param strategyName
     * @return
     */
    public EmailSenderStrategy getStrategy(String strategyName) {
        EmailSenderStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("No email sender strategy found for: " + strategyName);
        }
        return strategy;
    }

    /**
     * use the specified strategy to send a reset password email
     *
     * @param strategyName
     * @param to
     * @param resetUrl
     */
    public void sendResetPasswordEmail(String strategyName, String to, String resetUrl) {
        EmailSenderStrategy strategy = getStrategy(strategyName);
        strategy.sendResetPasswordEmail(to, resetUrl);
    }
}
