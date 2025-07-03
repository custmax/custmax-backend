package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.website.CreateWebsiteRequest;

import java.util.List;
import java.util.Map;

public interface WebSiteService {
    Object createWebsite(CreateWebsiteRequest request, Long userId);

    List<?> listUserWebsites(Long userId);

    Object getWebsiteDetails(Long id, Long userId);

    Object updateWebsite(Long id, Map<String, Object> request, Long userId);

    void deleteWebsite(Long id, Long userId);
}
