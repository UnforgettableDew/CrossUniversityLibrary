package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.auth.AuthenticationRequest;
import com.crossuniversity.securityservice.auth.AuthenticationResponse;
import com.crossuniversity.securityservice.auth.StudentRegistrationRequest;
import com.crossuniversity.securityservice.dto.CredentialDTO;
import com.crossuniversity.securityservice.entity.University;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.entity.UserCredentials;
import com.crossuniversity.securityservice.entity.UserRole;
import com.crossuniversity.securityservice.exception.UniversityNotFoundException;
import com.crossuniversity.securityservice.exception.UserAlreadyExistsException;
import com.crossuniversity.securityservice.repository.UniversityRepository;
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
    private final UniversityRepository universityRepository;
    private final SecurityUtils securityUtils;
    private final MailService mailService;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 UserDetailsService userDetailsService,
                                 UserCredentialsRepository userCredentialsRepository,
                                 UserRoleRepository userRoleRepository,
                                 PasswordEncoder passwordEncoder,
                                 UniversityUserRepository universityUserRepository,
                                 UniversityRepository universityRepository,
                                 SecurityUtils securityUtils,
                                 MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userCredentialsRepository = userCredentialsRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.universityUserRepository = universityUserRepository;
        this.universityRepository = universityRepository;
        this.securityUtils = securityUtils;
        this.mailService = mailService;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse registerStudent(StudentRegistrationRequest request) {
        String email = request.getEmail();
        checkEmailExistence(email);
        String domain = getDomain(email);

        University university = universityRepository.findUniversityByDomain(domain);

        if (university != null) {
            UserCredentials studentCredentials = saveUser(request.getEmail(), email, "STUDENT", university);

            AppUserDetails securityUser = AppUserDetails.convertToUserDetails(studentCredentials);

            String accessToken = jwtService.generateAccessToken(securityUser);
            String refreshToken = jwtService.generateRefreshToken(securityUser);

            return new AuthenticationResponse(accessToken, refreshToken);
        } else throw new UniversityNotFoundException("University domain = " + domain + " does not exist");
    }

    public CredentialDTO registerTeacher(String email) {
        checkEmailExistence(email);

        UniversityUser universityAdmin = securityUtils.getUserFromSecurityContextHolder();

        UserCredentials adminCredentials = universityAdmin.getUserCredentials();
        String domain = getDomain(adminCredentials.getEmail());

        if (domain.equals(getDomain(email))) {
            String randomPassword = securityUtils.generateRandomSequence();
            String role = "TEACHER";
            saveUser(email, randomPassword, role, universityRepository.findUniversityByDomain(domain));
            mailService.sendEmail(email, RANDOM_PASSWORD_SUBJECT,
                    randomPasswordMessage(email, randomPassword, role));
            return new CredentialDTO(email, randomPassword);
        } else throw new UniversityNotFoundException("University domain = " + domain + " does not exist");
    }

    public CredentialDTO registerUniversityAdmin(String email) {
        checkEmailExistence(email);
        String domain = getDomain(email);
        University university = universityRepository.findUniversityByDomain(domain);

        if (university != null) {
            String randomPassword = securityUtils.generateRandomSequence();
            String role = "UNIVERSITY_ADMIN";
            saveUser(email, randomPassword, role, university);

            mailService.sendEmail(email, RANDOM_PASSWORD_SUBJECT,
                    randomPasswordMessage(email, randomPassword, role));
            return new CredentialDTO(email, randomPassword);
        } else throw new UniversityNotFoundException("University domain = " + domain + " does not exist");
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
                .space(role.equals("STUDENT") ? 1000.0 : 10000.0)
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


    private String getDomain(String email) {
        String regex = "@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (matcher.find())
            return matcher.group(1);
        else
            throw new IllegalArgumentException("Invalid university email");
    }
}
