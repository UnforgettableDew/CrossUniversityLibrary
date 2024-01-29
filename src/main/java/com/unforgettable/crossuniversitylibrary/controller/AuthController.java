package com.unforgettable.crossuniversitylibrary.controller;

import com.unforgettable.crossuniversitylibrary.auth.AuthenticationResponse;
import com.unforgettable.crossuniversitylibrary.dto.CredentialsDTO;
import com.unforgettable.crossuniversitylibrary.service.AuthenticationService;
import com.unforgettable.crossuniversitylibrary.enums.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.unforgettable.crossuniversitylibrary.constant.ValidationViolation.*;
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
    public ResponseEntity<CredentialsDTO> teacherRegistration(
            @NotBlank(message = BLANK_EMAIL)
            @Email(message = EMAIL_NOT_RECOGNIZED)
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService.superiorRegistration(email, UserRoles.TEACHER.name()), HttpStatus.CREATED);
    }

    @PostMapping("/university-admin-registration")
    public ResponseEntity<CredentialsDTO> universityAdminRegistration(
            @NotBlank(message = BLANK_EMAIL)
            @Email(message = EMAIL_NOT_RECOGNIZED)
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService
                .superiorRegistration(email, UserRoles.UNIVERSITY_ADMIN.name()), HttpStatus.CREATED);
    }
}
