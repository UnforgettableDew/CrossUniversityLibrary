package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/library")
    public ResponseEntity<List<Library>> getOwnLibraries(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        return new ResponseEntity<>(studentService.getOwnLibraries(token), HttpStatus.OK);
    }
}
