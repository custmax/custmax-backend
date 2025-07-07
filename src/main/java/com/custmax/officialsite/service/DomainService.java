package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.domain.DomainReponse;
import com.custmax.officialsite.dto.website.RegisterDomainRequest;
import com.custmax.officialsite.entity.user.CustomUserDetails;

import java.util.List;
import java.util.Map;

public interface DomainService {
    /**
     * Sends a registration email for the specified domain name to the administrator.
     * @param domainName the domain name to register
     * @param emailReceiver the email address of the administrator
     */
    void sendRegistrationEmail(String domainName, String emailReceiver);

    /**
     * Queries WHOIS information for the specified domain name.
     * @param domainName the domain name to query
     * @return a map containing WHOIS information
     */
    Map<String, Object> queryWhois(String domainName);

    /**
     * Updates settings for the specified domain.
     * @param request contains the domain settings to update
     * @return a map containing the updated settings
     */
    Map<String, Object> updateDomainSettings(RegisterDomainRequest request);

    /**
     * Checks the availability of the specified domain name.
     * @param domainName the domain name to check
     * @return a map containing the availability status and details
     */
    Boolean checkDomainAvailability(String domainName);

    List<DomainReponse> getCurrentUserDomains(CustomUserDetails userDetails);
}
