package com.ruberoo.user_service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@SuppressWarnings("unused")
@Component
public class JwtTokenGenerator {

    @Autowired
    private Environment env;

    public String generateToken(String username, Long userId) {
        long validityInMilliseconds = 86400000L; // 24 hours
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(username) // Use username as the subject
                .claim("userId", userId) // Custom claim for user ID
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key key() {
        // Prefer environment variable (RUBEROO_JWT_SECRET_KEY) for Docker/Compose setups,
        // then fallback to config property (ruberoo.jwt.secret-key).
        String secretKey = env.getProperty("RUBEROO_JWT_SECRET_KEY");
        if (secretKey == null || secretKey.isBlank()) {
            secretKey = env.getProperty("ruberoo.jwt.secret-key");
        }
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("JWT secret not configured. Set RUBEROO_JWT_SECRET_KEY env var or ruberoo.jwt.secret-key property.");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
