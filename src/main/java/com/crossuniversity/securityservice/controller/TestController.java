package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final MailService mailService;

    @Autowired
    public TestController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/hello")
    public String hello(){
        return "HELLO";
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendEmail(String context){
        mailService.sendEmail("sagittariusdew@gmail.com", "CrossUniLibrary", context);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
