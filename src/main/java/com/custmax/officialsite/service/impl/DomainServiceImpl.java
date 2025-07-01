package com.custmax.officialsite.service.impl;


import com.custmax.officialsite.service.DomainService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RestTemplate restTemplate;


    public Map<String, Object> queryWhois(String domain) {
        String url = "https://uapis.cn/api/whois.php?domain=" + domain;
        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("WHOIS query failed: " + e.getMessage());
        }
    }

    public void sendRegistrationEmail(String domain, Map<String, Object> whoisData) {
        try {
            // 读取HTML模板
            Resource resource = new ClassPathResource("domain_reg.html");
            String htmlTemplate = new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);

            // 替换模板中的占位符
            String htmlContent = htmlTemplate
                    .replace("{{domain}}", domain)
                    .replace("{{registrar}}", String.valueOf(whoisData.getOrDefault("LLC", "N/A")))
                    .replace("{{regDate}}", String.valueOf(whoisData.getOrDefault("reg_date", "N/A")))
                    .replace("{{expDate}}", String.valueOf(whoisData.getOrDefault("exp_date", "N/A")))
                    .replace("{{domainStatus}}", String.valueOf(whoisData.getOrDefault("domain_status", "N/A")));

            // 创建HTML邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo("hello@custmax.com");
            helper.setSubject("Domain Registration Request - " + domain);
            helper.setText(htmlContent, true); // true表示HTML格式

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}