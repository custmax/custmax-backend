package com.custmax.officialsite.service.domain.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custmax.officialsite.dto.domain.DomainDetailsResponse;
import com.custmax.officialsite.dto.domain.WhoisResponse;
import com.custmax.officialsite.entity.domain.Domain;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.mapper.DomainMapper;
import com.custmax.officialsite.service.domain.DomainService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;


@Slf4j
@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${domain.email-receiver}")
    private String domainRegEmailReceiver;

    @Autowired
    private DomainMapper domainMapper;


    @Override
    public WhoisResponse queryWhois(String domain) {
        String url = "https://uapis.cn/api/whois.php?domain=" + domain;
        try {
            return restTemplate.getForObject(url, WhoisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("WHOIS query failed: " + e.getMessage());
        }
    }


    @Override
    public Boolean checkDomainAvailability(String domainName) {
        LambdaQueryWrapper<Domain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Domain::getDomainName, domainName);
        Long count = domainMapper.selectCount(queryWrapper);
        return count.equals(0L);
    }

    @Override
    public List<DomainDetailsResponse> getCurrentUserDomains(CustomUserDetails customUserDetails) {
        LambdaQueryWrapper<Domain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Domain::getUserId, customUserDetails.getUserId());
        return domainMapper.selectList(queryWrapper)
                .stream()
                .map(domain -> {
                    DomainDetailsResponse response = new DomainDetailsResponse();
                    response.setId(domain.getId());
                    response.setDomainName(domain.getDomainName());
                    response.setDomainStatus(domain.getStatus());
                    response.setSslEnabled(domain.getSslEnabled());
                    return response;
                }).toList();
    }

    @Override
    public void sendRegistrationEmail(String domain, String emailReceiver) {
        try {
            // read HTML template from classpath
            Resource resource = new ClassPathResource("domain_reg.html");
            String htmlTemplate = new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);

            // replace placeholders in the template
            String htmlContent = htmlTemplate
                    .replace("{{domain}}", domain);

            // create html email
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(domainRegEmailReceiver);
            helper.setSubject("Domain Registration Request - " + domain);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}