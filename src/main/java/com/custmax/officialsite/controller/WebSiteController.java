package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.CreateWebsiteRequest;
import com.custmax.officialsite.entity.CustomUserDetails;
import com.custmax.officialsite.service.impl.WebSiteServiceImpl;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WebSiteController {

    @Autowired
    private WebSiteServiceImpl webSiteService;

    // Create New Website
    @PostMapping("/websites")
    public ResponseEntity<?> createWebsite(@RequestBody CreateWebsiteRequest request,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        // 调用 wp-cli 创建 WordPress 站点
        Object result = webSiteService.createWebsite(request, user.getUserId());
        return ResponseEntity.ok(result);
    }

    // Get Website Details
    @GetMapping("/websites/{id}")
    public ResponseEntity<?> getWebsiteDetails(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails user) {
        Object website = webSiteService.getWebsiteDetails(id, user.getUserId());
        return ResponseEntity.ok(website);
    }

    // Update Website
    @PutMapping("/websites/{id}")
    public ResponseEntity<?> updateWebsite(@PathVariable Long id,
                                           @RequestBody Map<String, Object> request,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        Object result = webSiteService.updateWebsite(id, request, user.getUserId());
        return ResponseEntity.ok(result);
    }

    // Delete Website
    @DeleteMapping("/websites/{id}")
    public ResponseEntity<?> deleteWebsite(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        webSiteService.deleteWebsite(id, user.getUserId());
        return ResponseEntity.noContent().build();
    }
}