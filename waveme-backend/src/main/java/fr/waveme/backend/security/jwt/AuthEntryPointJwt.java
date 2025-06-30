package fr.waveme.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthEntryPointJwt is a custom implementation of the AuthenticationEntryPoint interface.
 * Used to handle unauthorized access attempts by returning a JSON response.
 * It logs the error and sends a structured JSON response
 * containing the error details when an authentication exception occurs.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    // Déclaration d'un logger pour enregistrer les informations, erreurs, et débogages
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Méthode `commence` déclenchée automatiquement lorsqu'une tentative d'accès non autorisée
     * est détectée. Elle crée une réponse JSON contenant les détails de l'erreur.
     *
     * @param request           Requête HTTP reçue
     * @param response          Réponse HTTP renvoyée
     * @param authException     Exception déclenchée en cas d'erreur d'authentification
     * @throws IOException      Exception lancée si une erreur d'E/S se produit
     * @throws ServletException Exception lancée en cas d'erreur liée au servlet
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
