package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.auth.AuthenticationRequest;
import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.auth.RegistrationRequest;
import com.crossuniversity.securityservice.entity.AppUser;
import com.crossuniversity.securityservice.enums.UserRole;
import com.crossuniversity.securityservice.repository.UserRepository;
import com.crossuniversity.securityservice.security.AppUserDetails;
import com.crossuniversity.securityservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
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

        AppUser user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);
        AppUserDetails securityUser = AppUserDetails.convertToUserDetails(user);

        String accessToken = jwtService.generateAccessToken(securityUser);
        String refreshToken = jwtService.generateRefreshToken(securityUser);

        return new AuthenticationResponse(accessToken, refreshToken);
    }
}
