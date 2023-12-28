package com.crossuniversity.securityservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        List<String> excludeUrlPatterns = new ArrayList<>(Arrays.asList(
                "/auth",
                "/swagger-resources",
                "/swagger-ui",
                "/v3/api-docs"));
        String servletPath = request.getServletPath();
        for (String pattern : excludeUrlPatterns) {
            if (servletPath.contains(pattern)) {
                log.info("JwtAuthenticationFilter skipped path: " + servletPath);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = jwtService.checkHeader(header);
            String email = jwtService.extractEmail(token);

            log.info("Email was extracted: " + email);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(email);
                log.info("User with email = " + email + " was found in database");
                if (jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    log.info("User with email = " + email + " was authenticated");
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            BasicFilterExceptionResponse.generateFilterExceptionResponse(
                    request,
                    response,
                    BAD_REQUEST,
                    exception.getMessage());
        }
    }
}

