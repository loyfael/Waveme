package fr.waveme.backend.security.jwt;

import java.security.Key;
import java.util.Date;

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
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    /**
     * Génère un cookie contenant un JWT à partir des informations de l'utilisateur.
     *
     * @param userPrincipal Détails de l'utilisateur authentifié
     * @return Cookie contenant le JWT
     */
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api") // Chemin de validité du cookie
                .maxAge(24 * 60 * 60) // Durée de vie du cookie en secondes (24 heures)
                .httpOnly(true) // Empêche l'accès au cookie depuis le JavaScript côté client
                .build();
        return cookie;
    }

    /**
     * Crée un cookie "propre" sans jeton, utile pour la déconnexion.
     *
     * @return Cookie sans valeur de jeton pour effacer le JWT existant
     */
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api") // Même chemin que le cookie JWT
                .build();
        return cookie;
    }

    /**
     * Extrait le nom d'utilisateur du JWT.
     *
     * @param token Jeton JWT
     * @return Nom d'utilisateur contenu dans le jeton
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Génère la clé de signature pour le JWT en utilisant le secret configuré.
     *
     * @return Clé de signature HMAC
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Valide le JWT en vérifiant sa signature et son expiration.
     *
     * @param authToken Jeton JWT à valider
     * @return `true` si le jeton est valide, `false` sinon
     */
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

    /**
     * Génère un jeton JWT à partir d'un nom d'utilisateur.
     *
     * @param username Nom d'utilisateur
     * @return Jeton JWT signé
     */
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username) // Associe le nom d'utilisateur au jeton
                .setIssuedAt(new Date()) // Date de création du jeton
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Date d'expiration du jeton
                .signWith(key(), SignatureAlgorithm.HS512) // Signature HMAC avec SHA-512
                .compact();
    }
}
