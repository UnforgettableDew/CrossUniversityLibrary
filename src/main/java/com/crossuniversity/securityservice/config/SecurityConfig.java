package com.crossuniversity.securityservice.config;

import com.crossuniversity.securityservice.security.SecurityAccessDeniedHandler;
import com.crossuniversity.securityservice.security.SecurityAuthenticationEntryPoint;
import com.crossuniversity.securityservice.security.JwtAuthenticationFilter;
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
//                .exceptionHandling().accessDeniedHandler(new SecurityAccessDeniedHandler())
//                .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
//                .and()
                .authorizeHttpRequests()
                    .requestMatchers(
                            "/auth/**",
                            "/swagger-ui/**", "/v3/api-docs/**",
                            "/swagger-resources/**", "/test/**",
                            "/library/university/{universityId}").permitAll()
                .requestMatchers("/library/create-teacher")
                    .hasAnyRole("TEACHER", "UNIVERSITY_ADMIN", "GLOBAL_ADMIN")
                .requestMatchers("/library/**")
                    .hasAnyRole("STUDENT", "TEACHER", "UNIVERSITY_ADMIN", "GLOBAL_ADMIN")
                .requestMatchers("/university-admin/**")
                    .hasAnyRole("UNIVERSITY_ADMIN", "GLOBAL_ADMIN")
                .requestMatchers("/global-admin/**")
                    .hasRole("GLOBAL_ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
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
