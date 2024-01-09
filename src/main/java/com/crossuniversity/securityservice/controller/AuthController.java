package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.service.AuthenticationService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.crossuniversity.securityservice.constant.ValidationViolation.*;
import static com.crossuniversity.securityservice.enums.UserRoles.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Validated
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(
            @NotBlank(message = BLANK_EMAIL)
            @Email(message = EMAIL_NOT_RECOGNIZED)
            @RequestParam String email,
            @NotBlank(message = BLANK_PASSWORD)
            @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.authenticate(email, password), HttpStatus.OK);
    }

    @PostMapping("/student-registration")
    public ResponseEntity<AuthenticationResponse> register(
            @NotBlank(message = BLANK_EMAIL)
            @Email(message = EMAIL_NOT_RECOGNIZED)
            @RequestParam String email,
            @NotBlank(message = BLANK_PASSWORD)
            @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.studentRegistration(email, password), HttpStatus.CREATED);
    }

    @PostMapping("/teacher-registration")
    public ResponseEntity<CredentialDTO> teacherRegistration(
            @NotBlank(message = BLANK_EMAIL)
            @Email(message = EMAIL_NOT_RECOGNIZED)
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService.superiorRegistration(email, TEACHER.name()), HttpStatus.CREATED);
    }

    @PostMapping("/university-admin-registration")
    public ResponseEntity<CredentialDTO> universityAdminRegistration(
            @NotBlank(message = BLANK_EMAIL)
            @Email(message = EMAIL_NOT_RECOGNIZED)
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService
                .superiorRegistration(email, UNIVERSITY_ADMIN.name()), HttpStatus.CREATED);
    }
}
