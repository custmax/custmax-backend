package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.website.RegisterDomainRequest;
import com.custmax.officialsite.service.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${domain.email-receiver}")
    private String emailReceiver;

    /**
     * Registers a new domain by sending an email to the administrator.
     * @param request contains the domain name to register
     * @return ResponseEntity with registration status and details
     */
    @PostMapping("/domains")
    @Operation(summary = "Register a new domain")
    public ResponseEntity<Map<String, Object>> registerDomain(@RequestBody Map<String, Object> request) {
        String domainName = (String) request.get("domainName");

        if (domainName == null || domainName.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Domain name cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();

        try {
            domainService.sendRegistrationEmail(domainName, emailReceiver);
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
     * Retrieves details of a specific domain by its ID.
     * @param id
     * @return
     */
    @GetMapping("/domains/{id}")
    @Operation(summary = "Get domain details by ID")
    public ResponseEntity<Map<String, Object>> getDomainDetails(@PathVariable String id) {
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
     * Updates domain settings such as nameservers and DNS records.
     * @param request contains the updated domain settings
     * @return ResponseEntity with update status and details
     */
    @PutMapping("/domains")
    @Operation(summary = "Update domain settings")
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
     * @return ResponseEntity with deletion status and details
     */
    @DeleteMapping("/domains/{id}")
    @Operation(summary = "Delete a domain by ID")
    public ResponseEntity<Map<String, Object>> deleteDomain(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("status", "deleted");
        response.put("message", "domain delete successfully");
        response.put("deletedAt", new Date());
        return ResponseEntity.ok(response);
    }

    /**
     * Maps a domain to a website by their IDs.
     * @param websiteId
     * @param domainId
     * @return ResponseEntity with mapping status and details
     */
    @PostMapping("/websites/{websiteId}/domains/{domainId}")
    @Operation(summary = "Map a domain to a website")
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
     * Configures SSL for a domain.
     * @param id
     * @param request contains SSL configuration details
     * @return ResponseEntity with SSL configuration status and details
     */
    @PostMapping("/domains/{id}/ssl")
    @Operation(summary = "Configure SSL for a domain")
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
     * @param name The base domain name to check
     * @return ResponseEntity with availability results for each TLD
     */
    @GetMapping("/domains/search")
    @Operation(summary = "Search domain availability across multiple TLDs")
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

    /**
     * Checks if a specific domain name is available.
     * @param domainName
     * @return
     */
    @GetMapping("/domains/availability")
    @Operation(summary = "Check if a specific domain name is available")
    public ResponseEntity<Boolean> checkDomainAvailability(@RequestParam String domainName) {
        return ResponseEntity.ok(domainService.checkDomainAvailability(domainName));
    }
}