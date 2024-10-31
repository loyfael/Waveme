package fr.waveme.backend.security.jwt;

import fr.waveme.backend.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Classe `AuthTokenFilter` : un filtre personnalisé exécuté pour chaque requête,
 * servant à authentifier l'utilisateur en extrayant et en validant un jeton JWT.
 * Hérite de `OncePerRequestFilter`, ce qui garantit qu'il est exécuté une seule fois par requête.
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    // Injecte un utilitaire pour gérer les opérations liées au JWT
    @Autowired
    private JwtUtils jwtUtils;

    // Injecte le service pour charger les détails de l'utilisateur
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Déclare un logger pour capturer les erreurs et informations importantes
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * La méthode `doFilterInternal` est appelée pour chaque requête, où elle tente de valider
     * le JWT, d'authentifier l'utilisateur et de placer l'authentification dans le contexte de sécurité.
     *
     * @param request     Requête HTTP reçue
     * @param response    Réponse HTTP envoyée
     * @param filterChain Chaîne de filtres permettant de passer à l'étape suivante
     * @throws ServletException En cas d'erreur liée au servlet
     * @throws IOException      En cas d'erreur d'E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extrait le jeton JWT de la requête (typiquement depuis les cookies ou le header Authorization)
            String jwt = parseJwt(request);

            // Si le jeton est présent et valide
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Extrait le nom d'utilisateur à partir du JWT
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Charge les détails de l'utilisateur à partir du nom d'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crée un token d'authentification avec les informations de l'utilisateur
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // Aucun mot de passe requis ici, car le JWT est déjà validé
                                userDetails.getAuthorities() // Liste des rôles et autorisations de l'utilisateur
                        );

                // Associe des informations d'authentification détaillées à la requête HTTP
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Place l'authentification dans le contexte de sécurité de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Enregistre une erreur dans le logger en cas d'échec de l'authentification
            logger.error("Cannot set user authentication: {}", e);
        }

        // Continue l'exécution de la chaîne de filtres (passage de la requête au filtre suivant)
        filterChain.doFilter(request, response);
    }

    /**
     * Méthode `parseJwt` qui récupère le JWT de la requête HTTP (exemple : depuis les cookies ou les en-têtes).
     *
     * @param request Requête HTTP reçue
     * @return Jeton JWT sous forme de chaîne, ou `null` si aucun jeton n'est trouvé
     */
    private String parseJwt(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }
}
