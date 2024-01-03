package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.UserProfileDTO;
import com.crossuniversity.securityservice.service.PasswordChangingService;
import com.crossuniversity.securityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final PasswordChangingService passwordChangingService;
    private final UserService userService;

    @Autowired
    public UserController(PasswordChangingService passwordChangingService,
                          UserService userService) {
        this.passwordChangingService = passwordChangingService;
        this.userService = userService;
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

    @PutMapping("/update-profile")
    public ResponseEntity<UserProfileDTO> updateProfile(@RequestParam String userName){
        return new ResponseEntity<>(userService.updateProfile(userName), HttpStatus.OK);
    }

    @GetMapping("/{email}/profile")
    public ResponseEntity<UserProfileDTO> getUserProfileByEmail(@PathVariable String email){
        return new ResponseEntity<>(userService.getUserProfileByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileDTO> myProfile(){
        return new ResponseEntity<>(userService.myProfile(), HttpStatus.OK);
    }
}
