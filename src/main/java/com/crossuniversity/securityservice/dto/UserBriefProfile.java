package com.crossuniversity.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "An object representing a brief user profile")
public class UserBriefProfile {
    private Long id;
    private String userName;
    private String email;
}
