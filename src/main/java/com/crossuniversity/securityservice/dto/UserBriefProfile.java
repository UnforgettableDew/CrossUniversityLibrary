package com.crossuniversity.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserBriefProfile {
    private Long id;
    private String userName;
    private String email;
}
