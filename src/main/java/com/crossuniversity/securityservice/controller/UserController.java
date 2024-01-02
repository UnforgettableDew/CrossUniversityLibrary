package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.service.PasswordChangingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final PasswordChangingService passwordChangingService;

    @Autowired
    public UserController(PasswordChangingService passwordChangingService) {
        this.passwordChangingService = passwordChangingService;
    }

    @PostMapping("/confirm-old-password")
    public ResponseEntity<?> confirmOldPassword(@RequestParam String confirmedPassword) throws AccessException {
        passwordChangingService.confirmOldPassword(confirmedPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String copiedSecretCode,
                                            @RequestParam String newPassword) throws AccessException {
        passwordChangingService.changePassword(copiedSecretCode, newPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
