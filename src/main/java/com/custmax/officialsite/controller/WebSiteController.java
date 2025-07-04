package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.website.CreateWebsiteRequest;
import com.custmax.officialsite.dto.website.CreateWebsiteResponse;
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

    /**
     * Retrieves a list of all WordPress websites for the authenticated user.
     *
     * @param user the authenticated user details
     * @return ResponseEntity with the list of websites
     */
    @Operation(summary = "get current user's WordPress websites")
    @GetMapping("/websites/{id}")
    public ResponseEntity<?> getWebsiteDetails(@PathVariable Long id,
                                               @AuthenticationPrincipal CustomUserDetails user) {
        Object website = webSiteService.getWebsiteDetails(id, user.getUserId());
        return ResponseEntity.ok(website);
    }

    /**
     * Updates the details of an existing WordPress website.
     *
     * @param id the ID of the website to update
     * @param request the request containing updated website details
     * @param user the authenticated user details
     * @return ResponseEntity with the result of the update operation
     */
    @PutMapping("/websites/{id}")
    public ResponseEntity<?> updateWebsite(@PathVariable Long id,
                                           @RequestBody Map<String, Object> request,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        Object result = webSiteService.updateWebsite(id, request, user.getUserId());
        return ResponseEntity.ok(result);
    }

    /**
     * Deletes a WordPress website by its ID.
     *
     * @param id the ID of the website to delete
     * @param user the authenticated user details
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/websites/{id}")
    public ResponseEntity<?> deleteWebsite(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        webSiteService.deleteWebsite(id, user.getUserId());
        return ResponseEntity.noContent().build();
    }
}