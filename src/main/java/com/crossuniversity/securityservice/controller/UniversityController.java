package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.crossuniversity.securityservice.constant.ResponseCode.*;
import static com.crossuniversity.securityservice.constant.SwaggerConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/university")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
@Tag(name = "University Controller")
public class UniversityController {
    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
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
            @RequestParam String title,
            @RequestParam String domain) {
        return new ResponseEntity<>(universityService.registerUniversity(title, domain), HttpStatus.CREATED);
    }

}
