package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.website.CreateWebsiteRequest;
import com.custmax.officialsite.dto.website.CreateWebsiteResponse;
import com.custmax.officialsite.entity.user.CustomUserDetails;
import com.custmax.officialsite.service.website.impl.WebSiteServiceImpl;
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

    /**
     * Creates a new WordPress website for the authenticated user.
     *
     * @param request the request containing website creation details
     * @param user the authenticated user details
     * @return ResponseEntity with the result of the website creation
     */
    @Operation(summary = "Create a new WordPress website")
    @PostMapping("/websites")
    public ResponseEntity<CreateWebsiteResponse> createWebsite(@RequestBody CreateWebsiteRequest request,
                                                               @AuthenticationPrincipal CustomUserDetails user) {
        CreateWebsiteResponse result = webSiteService.createWebsite(request, user.getUserId());
        return ResponseEntity.ok(result);
    }
}