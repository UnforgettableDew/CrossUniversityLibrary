package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.entity.UserCredentials;
import com.crossuniversity.securityservice.exception.bad_request.NotMatchException;
import com.crossuniversity.securityservice.repository.UserCredentialsRepository;
import com.crossuniversity.securityservice.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.crossuniversity.securityservice.utils.MailMessage.*;

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

        mailService.addSecretCodes(email, secretCode);
        mailService.sendEmail(email, PASSWORD_CHANGE_SUBJECT,
                passwordChangeMessage(universityUser.getUserName(), secretCode));
    }

    public void changePassword(String copiedSecretCode, String newPassword) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        UserCredentials userCredentials = universityUser.getUserCredentials();

        log.info("Provided secret code for " + userCredentials.getEmail() + " is " + copiedSecretCode);

        if (!copiedSecretCode.equals(mailService.getSecretCodes().get(userCredentials.getEmail())))
            throw new NotMatchException("Provided secret code does not match with the code which was sent to the mail");

        userCredentials.setPassword(passwordEncoder.encode(newPassword));
        userCredentialsRepository.save(userCredentials);
    }
}
