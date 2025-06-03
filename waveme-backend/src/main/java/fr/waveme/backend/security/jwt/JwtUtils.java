package fr.waveme.backend.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import fr.waveme.backend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * JwtUtils provides utility methods for handling JSON Web Tokens (JWT).
 * It includes methods for generating, validating, and extracting information from JWTs.
 * This class is annotated with @Component to allow Spring to manage it as a bean.
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${waveme.app.jwtSecret}")
    private String jwtSecretUtf8;

    @Value("${waveme.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${waveme.app.jwtCookieName}")
    private String token;

    public ResponseCookie getCleanJwt() {
        try {
            logger.info("[getCleanJwt] Returning clean JWT cookie.");
            return ResponseCookie.from(token, "")
                    .path("/api")
                    .maxAge(0)
                    .httpOnly(true)
                    .build();
        } catch (Exception e) {
            logger.error("[getCleanJwt] Error generating clean JWT cookie: {}", e.getMessage(), e);
        }
        return null;
    }

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

    /**
     * Extracts the social user ID from the JWT token.
     *
     * @param token The JWT token.
     * @return The social user ID as a String, or null if not found.
     */
    public String getSocialUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object idObj = claims.get("id");
        return idObj != null ? idObj.toString() : null;
    }

    /**
     * Extracts the authenticated user ID from the JWT token.
     *
     * @param token The JWT token.
     * @return The authenticated user ID as a Long.
     * @throws IllegalArgumentException if the ID claim is missing or not a valid Long.
     */
    public Long getAuthUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object idObj = claims.get("id");
        if (idObj == null) {
            throw new IllegalArgumentException("ID claim is missing from JWT");
        }

        try {
            return Long.valueOf(idObj.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID claim in JWT is not a valid Long");
        }
    }

    /**
     * Validates the JWT token.
     *
     * @param authToken The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
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

    public String generateTokenFromUser(UserDetailsImpl userPrincipal) {
        try {
            String token = Jwts.builder()
                    .setSubject(userPrincipal.getUsername())
                    .claim("id", userPrincipal.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(key(), SignatureAlgorithm.HS512)
                    .compact();
            logger.debug("[generateTokenFromUser] JWT for user {}: {}", userPrincipal.getUsername(), token);
            return token;
        } catch (Exception e) {
            logger.error("[generateTokenFromUser] Error: {}", e.getMessage(), e);
            return null;
        }
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

    public String getJwtTokenName() {
        return token;
    }
}
