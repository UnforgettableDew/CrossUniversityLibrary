package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudentService {
    private final JwtService jwtService;
    private final UniversityUserRepository universityUserRepository;

    @Autowired
    public StudentService(JwtService jwtService,
                          UniversityUserRepository universityUserRepository) {
        this.jwtService = jwtService;
        this.universityUserRepository = universityUserRepository;
    }

    public List<Library> getOwnLibraries(String token) {
        String email = jwtService.extractEmail(token.substring("Bearer".length()));
        UniversityUser universityUser = universityUserRepository.findUniversityUserByEmail(email);
        log.info("User with email = " + email + " was found");
        return universityUser.getOwnLibraries();
    }
}
