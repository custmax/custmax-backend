package com.custmax.officialsite.dto;

import java.time.LocalDateTime;

public class WebsiteDTO {
    private Long id;
    private String name;
    private String permalinkStructure;
    private String status;
    private Boolean isMultisite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public WebsiteDTO() {}

    // getter 和 setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPermalinkStructure() { return permalinkStructure; }
    public void setPermalinkStructure(String permalinkStructure) { this.permalinkStructure = permalinkStructure; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsMultisite() { return isMultisite; }
    public void setIsMultisite(Boolean isMultisite) { this.isMultisite = isMultisite; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}