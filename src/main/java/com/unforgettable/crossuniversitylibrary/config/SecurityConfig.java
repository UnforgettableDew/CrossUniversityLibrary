package com.unforgettable.crossuniversitylibrary.config;


import com.unforgettable.crossuniversitylibrary.security.SecurityAccessDeniedHandler;
import com.unforgettable.crossuniversitylibrary.security.SecurityAuthenticationEntryPoint;
import com.unforgettable.crossuniversitylibrary.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.unforgettable.crossuniversitylibrary.enums.UserPermission.*;
import static com.unforgettable.crossuniversitylibrary.enums.UserRoles.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final String[] WHITE_LIST = {
            "/auth/authenticate",
            "/auth/student-registration",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/openapi.yaml",
            "/swagger-resources/**",
            "/test/**",
            "/library/university/**"
    };

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          UserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .accessDeniedHandler(new SecurityAccessDeniedHandler())
                        .authenticationEntryPoint(new SecurityAuthenticationEntryPoint()))
                .authorizeHttpRequests((authorizeHttpRequest) -> authorizeHttpRequest
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers("/library/create-with-access")
                        .hasAuthority(LIBRARY_CREATE_WITH_ACCESS.getPermissionName())
                        .requestMatchers("/user/**")
                        .hasAnyRole(STUDENT.name(), TEACHER.name(), UNIVERSITY_ADMIN.name(), GLOBAL_ADMIN.name())
                        .requestMatchers("/library/**")
                        .hasAnyRole(STUDENT.name(), TEACHER.name(), UNIVERSITY_ADMIN.name(), GLOBAL_ADMIN.name())
                        .requestMatchers("/auth/teacher-registration")
                        .hasAuthority(TEACHER_REGISTER.getPermissionName())
                        .requestMatchers("/auth/university-admin-registration",
                                "/university/**")
                        .hasRole(GLOBAL_ADMIN.name())
                        .anyRequest()
                        .authenticated());
        httpSecurity
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}
