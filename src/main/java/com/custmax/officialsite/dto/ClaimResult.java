package com.custmax.officialsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimResult {
    private String code;
    private boolean existed;

}