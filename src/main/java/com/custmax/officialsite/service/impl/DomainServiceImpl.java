package com.custmax.officialsite.service.impl;


import com.custmax.officialsite.dto.RegisterDomainRequest;
import com.custmax.officialsite.entity.SshServer;
import com.custmax.officialsite.mapper.SshServerMapper;
import com.custmax.officialsite.service.DomainService;
import com.custmax.officialsite.util.SshExecutor;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${website.wpcli.path}")
    private String wpPath;

    @Autowired
    private SshServerMapper sshServerMapper;

    public Map<String, Object> queryWhois(String domain) {
        String url = "https://uapis.cn/api/whois.php?domain=" + domain;
        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("WHOIS query failed: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> updateDomainSettings(RegisterDomainRequest request) {
        String oldUrl = request.getOldUrl();
        String newUrl = request.getNewUrl();

        SshServer server = sshServerMapper.selectById(1L);

        String cmd = String.format(
                "cd %s && wp search-replace '%s' '%s' --all-tables --allow-root",
                wpPath, oldUrl, newUrl
        );

        Map<String, Object> resultMap = new java.util.HashMap<>();
        try {
            String result = SshExecutor.exec(
                    server.getHost(),
                    server.getPort(),
                    server.getUsername(),
                    server.getPassword(),
                    cmd
            );
            resultMap.put("success", true);
            resultMap.put("output", result);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("error", e.getMessage());
        }
        return resultMap;
    }

    public void sendRegistrationEmail(String domain) {
        try {
            // 读取HTML模板
            Resource resource = new ClassPathResource("domain_reg.html");
            String htmlTemplate = new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);

            // 替换模板中的占位符
            String htmlContent = htmlTemplate
                    .replace("{{domain}}", domain);

            // 创建HTML邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo("1453631832@qq.com");
            helper.setSubject("Domain Registration Request - " + domain);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}