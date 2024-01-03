package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.model.UniversityRegisterRequest;
import com.crossuniversity.securityservice.service.AuthenticationService;
import com.crossuniversity.securityservice.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register-university")
    public ResponseEntity<UniversityDTO> registerUniversity(@RequestBody UniversityRegisterRequest request){
        return new ResponseEntity<>(universityService.registerUniversity(request), HttpStatus.CREATED);
    }

    @PostMapping("/register-university-admin")
    public ResponseEntity<CredentialDTO> registerUniversityAdmin(@RequestParam String email){
        return new ResponseEntity<>(authenticationService.registerUniversityAdmin(email), HttpStatus.CREATED);
    }
}
