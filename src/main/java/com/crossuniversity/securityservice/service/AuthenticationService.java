package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.entity.University;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.entity.UserCredentials;
import com.crossuniversity.securityservice.entity.UserRole;
import com.crossuniversity.securityservice.exception.not_found.UniversityNotFoundException;
import com.crossuniversity.securityservice.exception.bad_request.UserAlreadyExistsException;
import com.crossuniversity.securityservice.helper.UniversityHelper;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.repository.UserCredentialsRepository;
import com.crossuniversity.securityservice.repository.UserRoleRepository;
import com.crossuniversity.securityservice.security.AppUserDetails;
import com.crossuniversity.securityservice.security.JwtService;
import com.crossuniversity.securityservice.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.crossuniversity.securityservice.enums.UserRoles.*;
import static com.crossuniversity.securityservice.utils.MailMessage.*;

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
    private final UniversityHelper universityHelper;
    private final SecurityUtils securityUtils;
    private final MailService mailService;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 UserCredentialsRepository userCredentialsRepository,
                                 UserRoleRepository userRoleRepository,
                                 PasswordEncoder passwordEncoder,
                                 UniversityUserRepository universityUserRepository,
                                 UniversityHelper universityHelper,
                                 SecurityUtils securityUtils,
                                 MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.universityUserRepository = universityUserRepository;
        this.universityHelper = universityHelper;
        this.securityUtils = securityUtils;
        this.mailService = mailService;
    }

    public AuthenticationResponse authenticate(String email, String password) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        UserDetails user = userDetailsService.loadUserByUsername(email);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse studentRegistration(String email, String password) {
        checkEmailExistence(email);
        String domain = extractDomain(email);

        University university = universityHelper.getUniversityByDomain(domain);

        UserCredentials studentCredentials = saveUser(email, password, STUDENT.name(), university);

        AppUserDetails securityUser = AppUserDetails.convertToUserDetails(studentCredentials);

        String accessToken = jwtService.generateAccessToken(securityUser);
        String refreshToken = jwtService.generateRefreshToken(securityUser);

        return new AuthenticationResponse(accessToken, refreshToken);
    }


    public CredentialDTO superiorRegistration(String email, String role){
        checkEmailExistence(email);
        String domain = extractDomain(email);

        University university = universityHelper.getUniversityByDomain(domain);
        if(role.equals(TEACHER.name())){
            UniversityUser universityAdmin = securityUtils.getUserFromSecurityContextHolder();
            UserCredentials adminCredentials = universityAdmin.getUserCredentials();
            if (!domain.equals(extractDomain(adminCredentials.getEmail())))
                throw new UniversityNotFoundException("University domain = " + domain + " does not exist");
        }
        String randomPassword = securityUtils.generateRandomSequence();

        saveUser(email, randomPassword, role, university);

        mailService.sendEmail(email, RANDOM_PASSWORD_SUBJECT,
                randomPasswordMessage(email, randomPassword, role));
        return new CredentialDTO(email, randomPassword);
    }

    private UserCredentials saveUser(String email,
                                     String password,
                                     String role,
                                     University university) {
        UserRole userRole = userRoleRepository.findUserRoleByRoleName(role);

        UserCredentials userCredentials = UserCredentials.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(userRole)
                .build();

        UniversityUser user = UniversityUser.builder()
                .userName(email)
                .university(university)
                .space(role.equals(STUDENT.name()) ? 1000.0 : 10000.0)
                .userCredentials(userCredentials)
                .build();

        universityUserRepository.save(user);
        log.info("User with email = " + email + " was registered");
        return userCredentials;
    }


    private void checkEmailExistence(String email) {
        if (userCredentialsRepository.findByEmail(email).isPresent())
            throw new UserAlreadyExistsException("User with email = " + email + " already exists");
    }

    private String extractDomain(String email) {
        String regex = "@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.find())
            return matcher.group(1);
        else
            throw new IllegalArgumentException("No matches found");
    }

}
