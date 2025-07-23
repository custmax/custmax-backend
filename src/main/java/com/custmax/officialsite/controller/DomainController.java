package com.custmax.officialsite.controller;

import com.custmax.officialsite.dto.domain.RegisterDomainResponse;
import com.custmax.officialsite.dto.domain.WhoisResponse;
import com.custmax.officialsite.service.domain.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param domainName The name of the domain to register
     * @return ResponseEntity with registration status and details
     */
    @PostMapping("/domains")
    @Operation(summary = "Register a new domain")
    public ResponseEntity<RegisterDomainResponse> registerDomain(@RequestParam String domainName) {
        RegisterDomainResponse response = new RegisterDomainResponse();
        if (domainName == null || domainName.trim().isEmpty()) {
            response.setMessage("Domain name is required");
        } else {
            try {
                domainService.sendRegistrationEmail(domainName, emailReceiver);
                response.setMessage("Domain registration request sent successfully");
                response.setDomainName(domainName);
                response.setStatus("success");
            } catch (Exception e) {
                response.setMessage("Failed to send registration request: " + e.getMessage());
                response.setStatus("error");
            }
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Searches for domain availability across multiple TLDs.
     * @param name The base domain name to check
     * @return ResponseEntity with availability results for each TLD
     */
    @GetMapping("/domains/search")
    @Operation(summary = "Search domain availability across multiple TLDs")
    public ResponseEntity<WhoisResponse> verifyCustomDomain(@RequestParam String name) {
            return ResponseEntity.ok(domainService.queryWhois(name));
    }

    /**
     * Checks if a specific subdomain name is available in .chtrak.com TLD.
     * @param domainName
     * @return
     */
    @GetMapping("/domains/availability")
    @Operation(summary = "Check if a specific subdomain name is available in .chtrak.com TLD")
    public ResponseEntity<Boolean> checkDomainAvailability(@RequestParam String domainName) {
        return ResponseEntity.ok(domainService.checkDomainAvailability(domainName));
    }
}