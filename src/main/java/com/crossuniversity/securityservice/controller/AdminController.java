package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AuthenticationService authenticationService;


    @Autowired
    public AdminController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register-teacher")
    public ResponseEntity<CredentialDTO> registerTeacher(@RequestParam String email){
        return new ResponseEntity<>(authenticationService.registerTeacher(email), HttpStatus.CREATED);
    }
}
