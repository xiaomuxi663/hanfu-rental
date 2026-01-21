package com.hanfu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return Jwts.builder().claims(claims).subject(username)
                .issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey()).compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) {
        try {
            return !parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) { return false; }
    }

    public Long getUserId(String token) { return parseToken(token).get("userId", Long.class); }
    public String getUsername(String token) { return parseToken(token).getSubject(); }
    public String getRole(String token) { return parseToken(token).get("role", String.class); }
}
