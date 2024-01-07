package com.crossuniversity.securityservice.config;

import com.crossuniversity.securityservice.exception.not_found.UserNotFoundException;
import com.crossuniversity.securityservice.repository.UserCredentialsRepository;
import com.crossuniversity.securityservice.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityBeans {
    private final UserCredentialsRepository userCredentialsRepository;

    @Autowired
    public SecurityBeans(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> AppUserDetails.convertToUserDetails(
                userCredentialsRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("User with email=" + email + " not found")));
    }


}
