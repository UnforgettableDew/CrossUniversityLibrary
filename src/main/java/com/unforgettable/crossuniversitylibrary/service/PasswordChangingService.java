package com.unforgettable.crossuniversitylibrary.service;

import com.unforgettable.crossuniversitylibrary.entity.UniversityUser;
import com.unforgettable.crossuniversitylibrary.entity.UserCredentials;
import com.unforgettable.crossuniversitylibrary.exception.bad_request.NotMatchException;
import com.unforgettable.crossuniversitylibrary.repository.UserCredentialsRepository;
import com.unforgettable.crossuniversitylibrary.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.unforgettable.crossuniversitylibrary.utils.MailMessage.*;

@Service
@Slf4j
public class PasswordChangingService {
    private final SecurityUtils securityUtils;
    private final MailService mailService;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordChangingService(SecurityUtils securityUtils,
                                   MailService mailService,
                                   UserCredentialsRepository userCredentialsRepository,
                                   PasswordEncoder passwordEncoder) {
        this.securityUtils = securityUtils;
        this.mailService = mailService;
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void confirmOldPassword(String confirmedPassword) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        UserCredentials userCredentials = universityUser.getUserCredentials();

        String encodedRealPassword = userCredentials.getPassword();

        if (!passwordEncoder.matches(confirmedPassword, encodedRealPassword))
            throw new NotMatchException("Provided password does not match with the real one");

        String secretCode = securityUtils.generateRandomSequence();
        String email = userCredentials.getEmail();
        log.info("Generated secret code for " + email + " is " + secretCode);

        universityUser.setSecretCode(secretCode);
        mailService.sendEmail(email, PASSWORD_CHANGE_SUBJECT,
                passwordChangeMessage(universityUser.getUserName(), secretCode));
    }

    public void changePassword(String copiedSecretCode, String newPassword) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        UserCredentials userCredentials = universityUser.getUserCredentials();

        log.info("Provided secret code for " + userCredentials.getEmail() + " is " + copiedSecretCode);

        if (!copiedSecretCode.equals(universityUser.getSecretCode()))
            throw new NotMatchException("Provided secret code does not match with the code which was sent to the mail");

        userCredentials.setPassword(passwordEncoder.encode(newPassword));
        userCredentialsRepository.save(userCredentials);
    }
}
