package com.crossuniversity.securityservice.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegistrationRequest {
    private String email;
    private String password;
}
