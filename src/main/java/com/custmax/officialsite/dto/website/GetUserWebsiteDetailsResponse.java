package com.custmax.officialsite.dto.website;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetUserWebsiteDetailsResponse {
    public enum Status {
        DRAFT, PUBLISHED, ARCHIVED
    }
    private Long id;
    private String name;
    private String permalinkStructure;
    private String status;
    private Boolean isMultisite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

