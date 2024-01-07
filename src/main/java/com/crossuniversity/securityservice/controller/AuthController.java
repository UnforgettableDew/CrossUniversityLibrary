package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.crossuniversity.securityservice.constant.ResponseCode.*;
import static com.crossuniversity.securityservice.constant.SwaggerConstant.*;
import static com.crossuniversity.securityservice.enums.UserRoles.TEACHER;
import static com.crossuniversity.securityservice.enums.UserRoles.UNIVERSITY_ADMIN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
@Tag(name = "Authentication Controller")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "User authentication",
            description = "This API endpoint is designed for user authentication, allowing registered users to authenticate " +
                    "themselves by providing their email address and password. The method accepts parameters for the student's " +
                    "email and password. The response includes an AuthenticationResponse containing the authentication token.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = JWT_TOKEN_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = BAD_REQUEST,
                            description = BAD_REQUEST_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BAD_REQUEST_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(
            @Parameter(description = "The email for login")
            @RequestParam String email,
            @Parameter(description = "The password for login")
            @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.authenticate(email, password), HttpStatus.OK);
    }

    @Operation(
            summary = "Student registration",
            description = "This API endpoint is designed for student registration, allowing new students to register " +
                    "by providing their email address and password. The method accepts parameters for the student's email and password. " +
                    "The response includes an AuthenticationResponse containing the authentication token.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = JWT_TOKEN_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = BAD_REQUEST,
                            description = BAD_REQUEST_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BAD_REQUEST_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/student-registration")
    public ResponseEntity<AuthenticationResponse> register(
            @Parameter(description = "Email address for student registration")
            @RequestParam String email,
            @Parameter(description = "Password for student registration")
            @RequestParam String password) {
        return new ResponseEntity<>(authenticationService.studentRegistration(email, password), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Teacher registration by university administrators",
            description = "This API endpoint is accessible to users with the role of university administrators, " +
                    "specifically allowing them to register teachers in the system using their email addresses. " +
                    "The response includes a CredentialDTO containing the registration information(emaiil and generated password). " +
                    "After successful registration, the system sends an email to the teacher containing login credentials.",
            responses = {
                    @ApiResponse(
                            responseCode = OK,
                            description = SUCCESSFUL_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = CREDENTIALS_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = BAD_REQUEST,
                            description = BAD_REQUEST_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BAD_REQUEST_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/teacher-registration")
    public ResponseEntity<CredentialDTO> teacherRegistration(
            @Parameter(description = "Email address of the teacher to be registered")
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService.superiorRegistration(email, TEACHER.name()), HttpStatus.CREATED);
    }

    @Operation(
            summary = "University administrator registration by global administrators",
            description = "This API endpoint is accessible to users with the role of global administrators, " +
                    "allowing them to register university administrators in the system using their email addresses. " +
                    "The response includes a CredentialDTO containing the registration information(emaiil and generated password). " +
                    "After successful registration, the system sends an email to the university administrator " +
                    "containing login credentials.",
            responses = {
                    @ApiResponse(
                            responseCode = CREATED,
                            description = CREATED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = CREDENTIALS_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = BAD_REQUEST,
                            description = BAD_REQUEST_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BAD_REQUEST_EXCEPTION)
                            )
                    ),
                    @ApiResponse(
                            responseCode = NOT_FOUND,
                            description = NOT_FOUND_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = NOT_FOUND_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/university-admin-registration")
    public ResponseEntity<CredentialDTO> universityAdminRegistration(
            @Parameter(description = "Email address of the university admin to be registered")
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService
                .superiorRegistration(email, UNIVERSITY_ADMIN.name()), HttpStatus.CREATED);
    }
}
