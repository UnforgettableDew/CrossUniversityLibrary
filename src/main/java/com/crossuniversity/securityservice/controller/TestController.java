package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private final MailService mailService;
    private final S3Client s3Client;

    @Autowired
    public TestController(MailService mailService, S3Client s3Client) {
        this.mailService = mailService;
        this.s3Client = s3Client;
    }

    @GetMapping("/hello")
    public String hello() {
        return "HELLO";
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendEmail(String context) {
        mailService.sendEmail("sagittariusdew@gmail.com", "CrossUniLibrary", context);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/s3")
    public ResponseEntity<List<String>> getBuckets() {

        s3Client.listBuckets();
        List<String> buckets = s3Client.listBuckets().buckets()
                .stream()
                .map(Bucket::name)
                .toList();
        return new ResponseEntity<>(buckets, HttpStatus.OK);
    }
}
