package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.auth.AuthenticationRequest;
import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.auth.RegistrationRequest;
import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.entity.UserCredentials;
import com.crossuniversity.securityservice.entity.UserRole;
import com.crossuniversity.securityservice.exception.UserAlreadyExistsException;
import com.crossuniversity.securityservice.exception.UserNotFoundException;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.repository.UserCredentialsRepository;
import com.crossuniversity.securityservice.repository.UserRoleRepository;
import com.crossuniversity.securityservice.security.AppUserDetails;
import com.crossuniversity.securityservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UniversityUserRepository universityUserRepository;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 UserCredentialsRepository userCredentialsRepository,
                                 UserRoleRepository userRoleRepository,
                                 PasswordEncoder passwordEncoder,
                                 UniversityUserRepository universityUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.universityUserRepository = universityUserRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse register(RegistrationRequest request) {
        String email = request.getEmail();

        checkEmailExistence(email);

        UserRole userRole = userRoleRepository.findUserRoleByRoleName("STUDENT");
        UserCredentials user = UserCredentials.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        userCredentialsRepository.save(user);
        log.info("User with email = " + email + " was registered");

        AppUserDetails securityUser = AppUserDetails.convertToUserDetails(user);

        String accessToken = jwtService.generateAccessToken(securityUser);
        String refreshToken = jwtService.generateRefreshToken(securityUser);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public CredentialDTO registerTeacher(String email) {
        checkEmailExistence(email);
        UniversityUser universityUser = getUserFromSecurityContextHolder();

        UserCredentials adminCredentials = universityUser.getUserCredentials();
        String roleName = adminCredentials.getRole().getRoleName();

        if (roleName.equals("UNIVERSITY_ADMIN") && getDomain(adminCredentials.getEmail()).equals(getDomain(email))) {
            UserRole userRole = userRoleRepository.findUserRoleByRoleName("TEACHER");

            String randomPassword = getRandomString();

            UserCredentials user = UserCredentials.builder()
                    .email(email)
                    .password(passwordEncoder.encode(randomPassword))
                    .role(userRole)
                    .build();

            userCredentialsRepository.save(user);
            log.info("Teacher with email = " + email + " was registered");
            return new CredentialDTO(email, randomPassword);
        } else throw new IllegalArgumentException("Invalid university email");
    }

    private String getRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            stringBuilder.append(randomChar);
        }


        return stringBuilder.toString();
    }

    private void checkEmailExistence(String email) {
        if (userCredentialsRepository.findByEmail(email).isPresent())
            throw new UserAlreadyExistsException("User with email = " + email + " already exists");
    }

    private UniversityUser getUserFromSecurityContextHolder() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User with email = " + email + " was found");

        return universityUserRepository.findUniversityUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email = " + email + " doesn't exists"));
    }

    private String getDomain(String email) {
        String regex = "@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) {
            String group = matcher.group(1);
            return group;
        } else {
            throw new IllegalArgumentException("Invalid university email");
        }
    }

}
