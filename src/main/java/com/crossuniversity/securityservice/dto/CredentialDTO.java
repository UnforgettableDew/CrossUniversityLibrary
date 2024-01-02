package com.crossuniversity.securityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CredentialDTO {
    private String email;
    private String password;
}
