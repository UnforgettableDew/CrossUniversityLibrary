package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.crossuniversity.securityservice.utils.ResponseCode.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.BAD_REQUEST_EXCEPTION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/university-admin")
@Tag(name = "Controller for University Admin")
public class UniversityAdminController {
    private final AuthenticationService authenticationService;


    @Autowired
    public UniversityAdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
    @PostMapping("/register-teacher")
    public ResponseEntity<CredentialDTO> registerTeacher(
            @Parameter(description = "Email address of the teacher to be registered")
            @RequestParam String email){
        return new ResponseEntity<>(authenticationService.registerTeacher(email), HttpStatus.CREATED);
    }
}
