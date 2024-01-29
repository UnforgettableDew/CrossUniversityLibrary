package com.unforgettable.crossuniversitylibrary.config;

import com.unforgettable.crossuniversitylibrary.security.SecurityAccessDeniedHandler;
import com.unforgettable.crossuniversitylibrary.security.SecurityAuthenticationEntryPoint;
import com.unforgettable.crossuniversitylibrary.security.JwtAuthenticationFilter;
import com.unforgettable.crossuniversitylibrary.enums.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
        httpSecurity.csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(new SecurityAccessDeniedHandler())
                .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/auth/authenticate",
                        "/auth/student-registration",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/openapi.yaml",
                        "/swagger-resources/**",
                        "/test/**",
                        "/library/university/**").permitAll()
                .requestMatchers("/library/create-with-access")
                .hasAnyRole(UserRoles.TEACHER.name(), UserRoles.UNIVERSITY_ADMIN.name(), UserRoles.GLOBAL_ADMIN.name())
                .requestMatchers("/user/**")
                .hasAnyRole(UserRoles.STUDENT.name(), UserRoles.TEACHER.name(), UserRoles.UNIVERSITY_ADMIN.name(), UserRoles.GLOBAL_ADMIN.name())
                .requestMatchers("/library/**")
                .hasAnyRole(UserRoles.STUDENT.name(), UserRoles.TEACHER.name(), UserRoles.UNIVERSITY_ADMIN.name(), UserRoles.GLOBAL_ADMIN.name())
                .requestMatchers("/auth/teacher-registration")
                .hasAnyRole(UserRoles.UNIVERSITY_ADMIN.name(), UserRoles.GLOBAL_ADMIN.name())
                .requestMatchers("/auth/university-admin-registration",
                        "/university/**")
                .hasRole(UserRoles.GLOBAL_ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.cors();
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
