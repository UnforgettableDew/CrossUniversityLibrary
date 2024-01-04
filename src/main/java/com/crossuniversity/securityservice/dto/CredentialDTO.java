package com.crossuniversity.securityservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "An object representing a credentials")
public class CredentialDTO {
    private String email;
    private String password;
}
