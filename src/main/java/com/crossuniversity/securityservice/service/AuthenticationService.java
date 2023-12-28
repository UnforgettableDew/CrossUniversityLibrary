package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.auth.AuthenticationRequest;
import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.auth.RegistrationRequest;
import com.crossuniversity.securityservice.entity.UserCredentials;
import com.crossuniversity.securityservice.entity.UserRole;
import com.crossuniversity.securityservice.exception.UserAlreadyExistsException;
import com.crossuniversity.securityservice.repository.UserCredentialsRepository;
import com.crossuniversity.securityservice.repository.UserRoleRepository;
import com.crossuniversity.securityservice.security.AppUserDetails;
import com.crossuniversity.securityservice.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 UserCredentialsRepository userCredentialsRepository,
                                 UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
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

        if(userCredentialsRepository.findByEmail(email).isPresent())
            throw new UserAlreadyExistsException("User with email = " + email + " already exists");

        UserRole userRole = userRoleRepository.findUserRoleByRoleName(request.getRole());
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
}
