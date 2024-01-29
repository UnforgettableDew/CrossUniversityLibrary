package com.unforgettable.crossuniversitylibrary.controller;

import com.unforgettable.crossuniversitylibrary.dto.UserProfileDTO;
import com.unforgettable.crossuniversitylibrary.service.PasswordChangingService;
import com.unforgettable.crossuniversitylibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class UserController {
    private final PasswordChangingService passwordChangingService;
    private final UserService userService;

    @Autowired
    public UserController(PasswordChangingService passwordChangingService,
                          UserService userService) {
        this.passwordChangingService = passwordChangingService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> profile() {
        return new ResponseEntity<>(userService.profile(), HttpStatus.OK);
    }

    @GetMapping("/{email}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfileByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserProfileByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/confirm-old-password")
    public ResponseEntity<?> confirmOldPassword(@RequestParam String confirmedPassword) {
        passwordChangingService.confirmOldPassword(confirmedPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String copiedSecretCode,
                                            @RequestParam String newPassword) {
        passwordChangingService.changePassword(copiedSecretCode, newPassword);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileDTO> updateProfile(@RequestParam String userName) {
        return new ResponseEntity<>(userService.updateProfile(userName), HttpStatus.OK);
    }
}
