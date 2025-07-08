package com.custmax.officialsite.service.email.impl;

import com.custmax.officialsite.service.email.EmailSenderStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: Vincent
 * @CreateTime: 2025-07-08
 * @Description: BillionMail Mail Sender
 * @Version: 1.0
 */
@Component("billionMail")
public class BillionMailSenderImpl implements EmailSenderStrategy {

    @Override
    public void sendResetPasswordEmail(String to, String resetUrl) {
        // Prepare email API request
        String apiUrl = "https://mail.cusob.com/api/batch_mail/api/send";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", "1407534e6b439bf91bb146c56a66287bc9b29c8eb5c139356d0624082ba72651");
        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
//        System.out.println(response);
        // Optional: handle response status and errors
    }
}
