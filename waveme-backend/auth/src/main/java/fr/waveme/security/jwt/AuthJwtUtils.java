package fr.waveme.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import fr.waveme.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class AuthJwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(AuthJwtUtils.class);

    @Value("${waveme.app.jwtSecret}")
    private String jwtSecretUtf8;

    @Value("${waveme.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${waveme.app.jwtCookieName}")
    private String token;

    public String getJwtFromCookies(HttpServletRequest request) {
        try {
            Cookie cookie = WebUtils.getCookie(request, token);
            if (cookie != null) {
                logger.debug("JWT found in cookie: {}", cookie.getValue());
                return cookie.getValue();
            } else {
                logger.warn("No JWT cookie named '{}' found in request.", token);
            }
        } catch (Exception e) {
            logger.error("[getJwtFromCookies] Error extracting JWT: {}", e.getMessage(), e);
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        try {
            if (userPrincipal == null) {
                logger.warn("[generateJwt] User principal is null, returning clean cookie.");
                return getCleanJwt();
            }
            String jwt = generateTokenFromUser(userPrincipal);
            logger.debug("[generateJwt] JWT generated for user {}: {}", userPrincipal.getUsername(), jwt);
            return ResponseCookie.from(token, jwt)
                    .path("/api")
                    .maxAge(24 * 60 * 60)
                    .httpOnly(true)
                    .build();
        } catch (Exception e) {
            logger.error("[generateJwt] Error: {}", e.getMessage(), e);
            return getCleanJwt();
        }
    }

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

    public String generateTokenFromUsername(String username) {
        try {
            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(key(), SignatureAlgorithm.HS512)
                    .compact();
            logger.debug("[generateTokenFromUsername] JWT for username {}: {}", username, token);
            return token;
        } catch (Exception e) {
            logger.error("[generateTokenFromUsername] Error: {}", e.getMessage(), e);
            return null;
        }
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

    public String getJwtCookieName() {
        return token;
    }
}
