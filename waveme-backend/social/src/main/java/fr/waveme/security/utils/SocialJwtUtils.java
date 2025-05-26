package fr.waveme.security.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocialJwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(SocialJwtUtils.class);

    @Value("${waveme.app.jwtSecret}")
    private String jwtSecretUtf8;

    @Value("${waveme.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${waveme.app.jwtCookieName}")
    private String token;

    public String getUserNameFromJwtToken(String token) {
        try {
            String username = Jwts.parserBuilder().setSigningKey(key()).build()
                    .parseClaimsJws(token).getBody().getSubject();
            logger.debug("[getUserNameFromJwtToken] Extracted username: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("[getUserNameFromJwtToken] Error: {}", e.getMessage(), e);
            return null;
        }
    }

    public Long getUserIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("id", Integer.class).longValue();
        } catch (Exception e) {
            logger.error("[JwtUtils] Cannot extract userId from token: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            logger.debug("[validateJwtToken] JWT is valid.");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("[validateJwtToken] Invalid signature or malformed token: {}", e.getMessage(), e);
        } catch (ExpiredJwtException e) {
            logger.error("[validateJwtToken] Token expired: {}", e.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            logger.error("[validateJwtToken] Unsupported token: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error("[validateJwtToken] Empty claims string: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("[validateJwtToken] Unexpected error: {}", e.getMessage(), e);
        }
        return false;
    }

    private Key key() {
        try {
            byte[] keyBytes = jwtSecretUtf8.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 64) {
                logger.warn("[key] JWT secret too short ({} bytes), may not be secure enough for HS512.", keyBytes.length);
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("[key] Error creating HMAC key from secret: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create signing key", e);
        }
    }

    public String getJwtCookieName() {
        return token;
    }
}
