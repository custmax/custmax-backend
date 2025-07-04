package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.website.RegisterDomainRequest;
import com.custmax.officialsite.service.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Domain Management", description = "Manage domain registrations and settings")
public class DomainController {

    @Autowired
    private DomainService domainService;

    /**
     * Registers a new domain and sends an email to the administrator.
     * @param request
     * @return
     */
    @Operation(summary = "Register a new domain")
    @PostMapping("/domains")
    public ResponseEntity<?> registerDomain(@RequestBody Map<String, Object> request) {
        String domainName = (String) request.get("domainName");

        if (domainName == null || domainName.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Domain name cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();

        try {
            // Send email to hello@custmax.com
            domainService.sendRegistrationEmail(domainName);
            response.put("domainId", "dom_" + System.currentTimeMillis());
            response.put("domainName", domainName);
            response.put("status", "pending");
            response.put("message", "Domain registration request submitted, email sent to administrator");
            response.put("registeredAt", new Date());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("error", "Domain registration request failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get details of a specific domain by its ID.
     * @param id
     * @return
     */
    @Operation(summary = "Get domain details")
    @GetMapping("/domains/{id}")
    public ResponseEntity<?> getDomainDetails(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("domainName", "example.com");
        response.put("status", "active");
        response.put("sslEnabled", true);
        response.put("registeredAt", new Date());
        response.put("expiresAt", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));
        response.put("nameservers", Arrays.asList("ns1.example.com", "ns2.example.com"));
        response.put("dnsRecords", Arrays.asList(
                Map.of("type", "A", "name", "@", "value", "192.168.1.1"),
                Map.of("type", "CNAME", "name", "www", "value", "example.com")
        ));
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the settings of an existing domain.
     * @param request
     * @return
     */
    @Operation(summary = "Update domain settings")
    @PutMapping("/domains")
    public ResponseEntity<Map<String, Object>> updateDomainSettings(
            @RequestBody RegisterDomainRequest request) {
        Map<String, Object> response = domainService.updateDomainSettings(request);

        response.put("message", "domain update successfully");
        response.put("updatedAt", new Date());
        response.put("settings", request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a domain by its ID.
     * @param id
     * @return
     */
    @Operation(summary = "Delete a domain")
    @DeleteMapping("/domains/{id}")
    public ResponseEntity<Map<String, Object>> deleteDomain(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("status", "deleted");
        response.put("message", "domain delete successfully");
        response.put("deletedAt", new Date());
        return ResponseEntity.ok(response);
    }

    /**
     * Maps a domain to a specific website by their IDs.
     */
    @Operation(summary = "Map domain to a website")
    @PostMapping("/websites/{websiteId}/domains/{domainId}")
    public ResponseEntity<Map<String, Object>> mapDomainToWebsite(
            @PathVariable String websiteId,
            @PathVariable String domainId) {
        Map<String, Object> response = new HashMap<>();
        response.put("websiteId", websiteId);
        response.put("domainId", domainId);
        response.put("status", "mapped");
        response.put("message", "domain mapped to website successfully");
        response.put("mappedAt", new Date());
        return ResponseEntity.ok(response);
    }

    /**
     * Unmaps a domain from a specific website by their IDs.
     */
    @Operation(summary = "Configure SSL for a domain")
    @PostMapping("/domains/{id}/ssl")
    public ResponseEntity<Map<String, Object>> configureSsl(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("sslEnabled", true);
        response.put("sslStatus", "active");
        response.put("certificateType", request.getOrDefault("certificateType", "Let's Encrypt"));
        response.put("message", "SSL configured successfully");
        response.put("configuredAt", new Date());
        response.put("expiresAt", new Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000));
        return ResponseEntity.ok(response);
    }

    /**
     * Searches for domain availability across multiple TLDs.
     * @param name
     * @return
     */
    @Operation(summary = "Search domain availability")
    @GetMapping("/domains/search")
    public ResponseEntity<Map<String, Object>> searchDomainAvailability(@RequestParam String name) {
        List<String> tlds = Arrays.asList("com", "net", "cn", "org", "io");
        List<Map<String, Object>> results = new ArrayList<>();
        for (String tld : tlds) {
            String fullDomain = name + "." + tld;
            Map<String, Object> whoisData = domainService.queryWhois(fullDomain);
            int code = (int) whoisData.getOrDefault("code", 500);
            Map<String, Object> result;
            if (code == 200) {
                result = Map.of(
                        "domain", fullDomain,
                        "available", true,
                        "response", Map.of("code", 200)
                );
            } else {
                result = Map.of(
                        "domain", fullDomain,
                        "available", false,
                        "response", whoisData
                );
            }
            results.add(result);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        return ResponseEntity.ok(response);
    }
}