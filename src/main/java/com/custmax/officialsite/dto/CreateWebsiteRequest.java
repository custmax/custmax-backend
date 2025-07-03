package com.custmax.officialsite.dto;

import lombok.Data;

@Data
public class CreateWebsiteRequest {
    private String name;
    private String domain;
    private String title;
    private String description;
    private String industry;
}