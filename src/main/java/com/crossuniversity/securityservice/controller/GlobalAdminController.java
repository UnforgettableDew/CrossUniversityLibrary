package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.auth.UniversityRegisterRequest;
import com.crossuniversity.securityservice.service.AuthenticationService;
import com.crossuniversity.securityservice.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.crossuniversity.securityservice.utils.ResponseCode.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.*;
import static com.crossuniversity.securityservice.utils.SwaggerConstant.NOT_FOUND_EXCEPTION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/global-admin")
public class GlobalAdminController {
    private final UniversityService universityService;
    private final AuthenticationService authenticationService;

    @Autowired
    public GlobalAdminController(UniversityService universityService,
                                 AuthenticationService authenticationService) {
        this.universityService = universityService;
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "University registration by global administrators",
            description = "This API endpoint is accessible to users with the role of global administrators, " +
                    "allowing them to register new universities in the system. To register a university, users " +
                    "need to provide the title and domain information through a UniversityRegisterRequest in the request body.",
            responses = {
                    @ApiResponse(
                            responseCode = CREATED,
                            description = CREATED_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = UNIVERSITY_FULL_EXAMPLE)
                            )
                    ),
                    @ApiResponse(
                            responseCode = BAD_REQUEST,
                            description = BAD_REQUEST_DESCRIPTION,
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    examples = @ExampleObject(value = BAD_REQUEST_EXCEPTION)
                            )
                    )
            }
    )
    @PostMapping("/register-university")
    public ResponseEntity<UniversityDTO> registerUniversity(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = UNIVERSITY_REQUEST_EXAMPLE)
                    ))
            @RequestBody UniversityRegisterRequest request) {
        return new ResponseEntity<>(universityService.registerUniversity(request), HttpStatus.CREATED);
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
    @PostMapping("/register-university-admin")
    public ResponseEntity<CredentialDTO> registerUniversityAdmin(
            @Parameter(description = "Email address of the university admin to be registered")
            @RequestParam String email) {
        return new ResponseEntity<>(authenticationService.registerUniversityAdmin(email), HttpStatus.CREATED);
    }
}
