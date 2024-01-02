package com.crossuniversity.securityservice.utils;

import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.exception.UserNotFoundException;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class SecurityUtils {
    private final UniversityUserRepository universityUserRepository;

    @Autowired
    public SecurityUtils(UniversityUserRepository universityUserRepository) {
        this.universityUserRepository = universityUserRepository;
    }

    public UniversityUser getUserFromSecurityContextHolder() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User with email = " + email + " was found");

        return universityUserRepository.findUniversityUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email = " + email + " doesn't exists"));
    }

    public String generateRandomSequence() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}
