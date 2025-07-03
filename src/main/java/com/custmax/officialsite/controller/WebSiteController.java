package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.website.CreateWebsiteRequest;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.service.impl.WebSiteServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Website Management", description = "Manage WordPress websites")
public class WebSiteController {

    @Autowired
    private WebSiteServiceImpl webSiteService;

    @Operation(summary = "Create a new WordPress website")
    @PostMapping("/websites")
    public ResponseEntity<?> createWebsite(@RequestBody CreateWebsiteRequest request,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        Object result = webSiteService.createWebsite(request, user.getUserId());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "get current user's WordPress websites")
    @GetMapping("/websites/{id}")
    public ResponseEntity<?> getWebsiteDetails(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails user) {
        Object website = webSiteService.getWebsiteDetails(id, user.getUserId());
        return ResponseEntity.ok(website);
    }

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