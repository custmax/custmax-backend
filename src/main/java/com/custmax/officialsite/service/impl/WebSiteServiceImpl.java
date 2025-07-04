package com.custmax.officialsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.custmax.officialsite.dto.website.CreateWebsiteRequest;
import com.custmax.officialsite.dto.website.CreateWebsiteResponse;
import com.custmax.officialsite.entity.domain.Domain;
import com.custmax.officialsite.entity.subscription.Subscription;
import com.custmax.officialsite.entity.user.User;
import com.custmax.officialsite.entity.website.Website;
import com.custmax.officialsite.mapper.*;
import com.custmax.officialsite.service.WebSiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class WebSiteServiceImpl implements WebSiteService {
    @Value("${website.wpcli.host}")
    private String host;

    @Value("${website.wpcli.path}")
    private String wpPath;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private DomainMapper domainMapper;

    @Autowired
    private WebSiteMapper webSiteMapper;

    private String generateRandomPassword(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
    // Generates a username like "user_a8f3k2"
    public String generateRandomUsername() {
        String prefix = "admin_";
        String randomStr = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return prefix + randomStr;
    }
    @Override
    @Transactional
    public CreateWebsiteResponse createWebsite(CreateWebsiteRequest request, Long userId) {
        int port = 22;
        String sshUser = System.getenv("WPCLI_SSH_USER");
        String sshPassword = System.getenv("WPCLI_SSH_PASSWORD");

        // 1. Query user info
        User user = userMapper.selectById(userId);
        String adminName = generateRandomUsername();
        String adminEmail = user.getEmail();
        // for test
        adminEmail = "ccccc@qq.com";
        String networkAdminEmail = "clifford.xie@chtrak.com";
        // 2. Generate random password
        String adminPassword = generateRandomPassword(12);

        // 3. Build site URL and url
        String siteUrl = "https://" + request.getDomain() + ".chtrak.com";
        String url = request.getDomain() + ".chtrak.com";
        // 4. Build wp site create command
        String siteCmd = String.format(
                "wp site create --slug=%s --title=\"%s\" --email=%s --path=%s --porcelain --allow-root",
                request.getDomain(), request.getTitle(), networkAdminEmail, wpPath
        );

        // 5. Build wp user create command
        String userCmd = String.format(
                "wp user create %s %s --role=administrator --url=%s --user_pass=%s --path=%s --allow-root",
                adminName, adminEmail, url, adminPassword, wpPath
        );

        CreateWebsiteResponse response = new CreateWebsiteResponse();
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(sshUser, host, port);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Execute site create command
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(siteCmd);
            channel.setInputStream(null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            channel.setOutputStream(outputStream);
            channel.setErrStream(outputStream);
            channel.connect();
            while (!channel.isClosed()) {
                Thread.sleep(100);
            }
            log.info("Site create command executed: {}", siteCmd);
            log.info("Output: {}", outputStream.toString(StandardCharsets.UTF_8));
            channel.disconnect();

            // Execute user create command
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(userCmd);
            ByteArrayOutputStream userOutputStream = new ByteArrayOutputStream();
            channel.setOutputStream(userOutputStream);
            channel.setErrStream(userOutputStream);
            channel.connect();
            while (!channel.isClosed()) {
                Thread.sleep(100);
            }
            log.info("User create command executed: {}", userCmd);
            log.info("Output: {}", userOutputStream.toString(StandardCharsets.UTF_8));
            channel.disconnect();

            session.disconnect();

            response.setSuccess(true);
            response.setSiteurl(siteUrl);
            response.setAdminUser(user.getUsername());
            response.setAdminPassword(adminPassword);

            /**
             * 1. Insert domain into database
             * 2. Insert website into database
             */
            LambdaQueryWrapper<Subscription> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Subscription::getUserId, userId);
            Long subscriptionId = subscriptionMapper.selectOne(queryWrapper).getId();
            Domain domain = new Domain();
            domain.setUserId(userId);
            domain.setDomainName(request.getDomain() + ".chtrak.com");
            domain.setIsCustom(false);
            domain.setSubscriptionId(subscriptionId);
            domain.setStatus(Domain.Status.active);
            domain.setSslEnabled(true);
            domainMapper.insert(domain);
            Website website = new Website();
            website.setUserId(userId);
            website.setDomainId(domain.getId());
            website.setName(request.getName());
            website.setPermalinkStructure(siteUrl);
            website.setStatus("active");
            website.setIndustry(request.getIndustry());
            webSiteMapper.insert(website);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public List<?> listUserWebsites(Long userId) {
        return List.of();
    }

    @Override
    public Object getWebsiteDetails(Long id, Long userId) {
        return null;
    }

    @Override
    public Object updateWebsite(Long id, Map<String, Object> request, Long userId) {
        return null;
    }

    @Override
    public void deleteWebsite(Long id, Long userId) {

    }
}