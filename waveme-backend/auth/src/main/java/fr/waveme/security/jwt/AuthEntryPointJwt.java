package fr.waveme.security.jwt;

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

        // Enregistre un message d'erreur contenant les détails de l'exception d'authentification
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Définit le type de contenu de la réponse à JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Définit le code de statut HTTP à 401 (Non Autorisé)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Prépare un corps de réponse JSON contenant des détails sur l'erreur
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED); // Code de statut HTTP
        body.put("error", "Unauthorized"); // Message d'erreur de type "Unauthorized"
        body.put("message", authException.getMessage()); // Détails du message d'erreur
        body.put("path", request.getServletPath()); // Chemin de la requête initiale

        // Crée un objet ObjectMapper pour convertir la Map en JSON et l'envoyer dans la réponse
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body); // Écrit le JSON dans le flux de sortie de la réponse
    }
}
