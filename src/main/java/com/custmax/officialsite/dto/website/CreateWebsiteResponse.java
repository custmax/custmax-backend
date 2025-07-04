package com.custmax.officialsite.dto.website;

import lombok.Data;

@Data
public class CreateWebsiteResponse {
    String siteurl;
    String adminUser;
    String adminPassword;
    Boolean success;
}
