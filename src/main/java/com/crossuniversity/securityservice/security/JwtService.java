package com.crossuniversity.securityservice.security;

import com.crossuniversity.securityservice.entity.AppUser;
import com.crossuniversity.securityservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token.expiration}")
    private Long accessExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshExpiration;
    private final UserRepository userRepository;

    @Autowired
    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("token-type", "access-token");
        return buildToken(userDetails, accessExpiration, extraClaims);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("token-type", "refresh-token");
        return buildToken(userDetails, refreshExpiration, extraClaims);
    }

    private String buildToken(UserDetails userDetails, Long expiration, Map<String, Object> extraClaims) {
        extraClaims.put("authorities", userDetails.getAuthorities());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AppUser getUserByJwt(HttpServletRequest request) {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String email = extractEmail(jwtToken);
        return userRepository.findByEmail(email);
//                .orElseThrow(() -> new UsernameNotFoundException("User with email=" + email + " not found"));
    }

    public String checkHeader(String token){
        if(token == null || !token.startsWith("Bearer "))
            throw new IllegalArgumentException("Header is null or doesn't start with 'Bearer '");
        return token.substring("Bearer".length());
    }
}