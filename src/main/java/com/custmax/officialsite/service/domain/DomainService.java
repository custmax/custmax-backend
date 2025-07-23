package com.custmax.officialsite.service.domain;

import com.custmax.officialsite.dto.domain.DomainDetailsResponse;
import com.custmax.officialsite.dto.domain.WhoisResponse;
import com.custmax.officialsite.entity.user.CustomUserDetails;

import java.util.List;

public interface DomainService {
    /**
     * Sends a registration email for the specified domain name to the administrator.
     * @param domainName the domain name to register
     * @param emailReceiver the email address of the administrator
     */
    void sendRegistrationEmail(String domainName, String emailReceiver);

    /**
     * Queries WHOIS information for the specified domain name.
     *
     * @param domainName the domain name to query
     * @return a map containing WHOIS information
     */
    WhoisResponse queryWhois(String domainName);


    /**
     * Checks the availability of the specified domain name.
     * @param domainName the domain name to check
     * @return a map containing the availability status and details
     */
    Boolean checkDomainAvailability(String domainName);

    List<DomainDetailsResponse> getCurrentUserDomains(CustomUserDetails userDetails);
}
