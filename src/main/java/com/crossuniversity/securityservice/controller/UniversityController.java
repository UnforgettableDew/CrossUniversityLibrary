package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/university")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class UniversityController {
    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @PostMapping("/register-university")
    public ResponseEntity<UniversityDTO> registerUniversity(@RequestParam String title,
                                                            @RequestParam String domain) {
        return new ResponseEntity<>(universityService.registerUniversity(title, domain), HttpStatus.CREATED);
    }

}
