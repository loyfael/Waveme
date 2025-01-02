package fr.waveme.backend.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fr.waveme.backend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

/**
 * `JwtUtils` est une classe utilitaire pour gérer les opérations de génération,
 * validation et extraction de données d'un jeton JWT dans le contexte de la sécurité.
 */
@Component
public class JwtUtils {

    // Logger pour capturer les erreurs et informations liées aux jetons JWT
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Clé secrète pour signer le JWT, injectée depuis les propriétés de configuration
    @Value("${waveme.app.jwtSecret}")
    private String jwtSecret;

    // Durée de validité du JWT en millisecondes, injectée depuis les propriétés de configuration
    @Value("${waveme.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Nom du cookie où le JWT sera stocké, injecté depuis les propriétés de configuration
    @Value("${waveme.app.jwtCookieName}")
    private String jwtCookie;

    /**
     * Extrait le jeton JWT du cookie dans la requête HTTP.
     *
     * @param request Requête HTTP contenant le cookie
     * @return Valeur du JWT, ou `null` si le cookie n'est pas présent
     */
    public String getJwtFromCookies(HttpServletRequest request) {
        try {
            Cookie cookie = WebUtils.getCookie(request, jwtCookie);
            if (cookie != null) {
                return cookie.getValue();
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error extracting JWT from cookies: {}", e.getMessage());
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        try {
            if (userPrincipal == null) {
                return getCleanJwtCookie();
            }
            String jwt = generateTokenFromUsername(userPrincipal.getUsername());
            return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
        } catch (Exception e) {
            logger.error("Error generating JWT cookie: {}", e.getMessage());
            return getCleanJwtCookie();
        }
    }

    public ResponseCookie getCleanJwtCookie() {
        try {
            return ResponseCookie.from(jwtCookie, "")
                    .path("/api")
                    .maxAge(0) // Set maxAge to 0 to delete the cookie
                    .httpOnly(true) // Ensure the cookie is HTTP only
                    .build();
        } catch (Exception e) {
            logger.error("Error generating clean JWT cookie: {}", e.getMessage());
        }

        return null;
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key()).build()
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from JWT token: {}", e.getMessage());
            return null;
        }
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }
}
