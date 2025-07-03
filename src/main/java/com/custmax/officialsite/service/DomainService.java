package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.RegisterDomainRequest;

import java.util.Map;

public interface DomainService {
    void sendRegistrationEmail(String domainName);

    Map<String, Object> queryWhois(String domainName);

    Map<String, Object> updateDomainSettings(RegisterDomainRequest request);
}
