package com.custmax.officialsite.controller;

import com.custmax.officialsite.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @PostMapping("/domains")
    public ResponseEntity<Map<String, Object>> registerDomain(@RequestBody Map<String, Object> request) {
        String domainName = (String) request.get("domainName");

        if (domainName == null || domainName.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Domain name cannot be empty");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();

        try {
            // Call WHOIS query API
            Map<String, Object> whoisData = domainService.queryWhois(domainName);

            // Send email to hello@custmax.com
            domainService.sendRegistrationEmail(domainName, whoisData);

            response.put("domainId", "dom_" + System.currentTimeMillis());
            response.put("domainName", domainName);
            response.put("status", "pending");
            response.put("message", "Domain registration request submitted, email sent to administrator");
            response.put("registeredAt", new Date());
            response.put("whoisInfo", whoisData);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("error", "Domain registration request failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/domains/{id}")
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

    @PutMapping("/domains/{id}")
    public ResponseEntity<Map<String, Object>> updateDomainSettings(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("message", "域名设置更新成功");
        response.put("updatedAt", new Date());
        response.put("settings", request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/domains/{id}")
    public ResponseEntity<Map<String, Object>> deleteDomain(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("status", "deleted");
        response.put("message", "域名删除成功");
        response.put("deletedAt", new Date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/websites/{websiteId}/domains/{domainId}")
    public ResponseEntity<Map<String, Object>> mapDomainToWebsite(
            @PathVariable String websiteId,
            @PathVariable String domainId) {
        Map<String, Object> response = new HashMap<>();
        response.put("websiteId", websiteId);
        response.put("domainId", domainId);
        response.put("status", "mapped");
        response.put("message", "域名映射成功");
        response.put("mappedAt", new Date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/domains/{id}/ssl")
    public ResponseEntity<Map<String, Object>> configureSsl(
            @PathVariable String id,
            @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("domainId", id);
        response.put("sslEnabled", true);
        response.put("sslStatus", "active");
        response.put("certificateType", request.getOrDefault("certificateType", "Let's Encrypt"));
        response.put("message", "SSL配置成功");
        response.put("configuredAt", new Date());
        response.put("expiresAt", new Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000));
        return ResponseEntity.ok(response);
    }
}