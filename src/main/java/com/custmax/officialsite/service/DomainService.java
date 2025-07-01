package com.custmax.officialsite.service;

import java.util.Map;

public interface DomainService {
    void sendRegistrationEmail(String domainName, Map<String, Object> whoisData);

    Map<String, Object> queryWhois(String domainName);
}
