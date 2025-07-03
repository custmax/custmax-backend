package com.custmax.officialsite.service.impl;

import com.custmax.officialsite.service.WebSiteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jcraft.jsch.*;
import java.io.ByteArrayOutputStream;

@Service
public class WebSiteServiceImpl implements WebSiteService {
    @Value("${website.wpcli.host}")
    private String host;

    @Value("${website.wpcli.path}")
    private String wpPath;

    @Override
    public Object createWebsite(CreateWebsiteRequest request, Long userId) {
        int port = 22;
        String user = System.getenv("WPCLI_SSH_USER");
        String password = System.getenv("WPCLI_SSH_PASSWORD");

        // 构建 wp site create 命令
        String cmd = String.format(
                "wp site create --slug=%s --title=\"%s\" --email=admin@example.com --path=%s --porcelain --allow-root",
                request.getDomain(), request.getTitle(), wpPath
        );

        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd);
            channel.setInputStream(null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            channel.setOutputStream(outputStream);
            channel.setOutputStream(outputStream);
            channel.setErrStream(outputStream);
            channel.connect();

            while (!channel.isClosed()) {
                Thread.sleep(100);
            }
            String result = outputStream.toString();
            channel.disconnect();
            session.disconnect();

            resultMap.put("success", true);
            resultMap.put("output", result);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("error", "Failed to create website: " + e.getMessage());
        }
        return resultMap;
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
