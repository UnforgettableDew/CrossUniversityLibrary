package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.auth.AuthenticationRequest;
import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.auth.StudentRegistrationRequest;
import com.crossuniversity.securityservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.crossuniversity.securityservice.utils.ResponseCode.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthController {
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "User authentication",
            description = "This API endpoint is designed for user authentication, allowing registered users to authenticate " +
                    "themselves. Users need to provide valid credentials through an AuthenticationRequest in the request body. " +
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
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = CREDENTIALS_EXAMPLE)
                    ))
            @RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @Operation(
            summary = "Student registration",
            description = "This API endpoint is designed for student registration, allowing new students to register " +
                    "and obtain authentication credentials. Users need to provide valid registration information through " +
                    "a StudentRegistrationRequest in the request body. The response includes an AuthenticationResponse " +
                    "containing the authentication token.",
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
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = CREDENTIALS_EXAMPLE)
                    ))
            @RequestBody StudentRegistrationRequest request) {
        return new ResponseEntity<>(authenticationService.registerStudent(request), HttpStatus.CREATED);
    }
}
