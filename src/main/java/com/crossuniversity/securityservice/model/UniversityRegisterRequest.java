package com.crossuniversity.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UniversityRegisterRequest {
    private String title;
    private String domain;
}
