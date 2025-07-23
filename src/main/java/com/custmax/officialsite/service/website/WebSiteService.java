package com.custmax.officialsite.service.website;

import com.custmax.officialsite.dto.website.CreateWebsiteRequest;
import com.custmax.officialsite.dto.website.CreateWebsiteResponse;
import com.custmax.officialsite.dto.website.GetUserWebsiteDetailsResponse;

import java.util.List;


public interface WebSiteService {
    CreateWebsiteResponse createWebsite(CreateWebsiteRequest request, Long userId);
    List<GetUserWebsiteDetailsResponse> getUserWebsites(Long userId);
}
