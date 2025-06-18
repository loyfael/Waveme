package fr.waveme.backend.auth.crud.controller;

import fr.waveme.backend.auth.payload.request.LoginRequest;
import fr.waveme.backend.auth.payload.response.UserInfoResponse;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthLoginTest {

    @Test
    void login_shouldReturnUserInfoResponse() {
        // Mocks
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtUtils jwtUtils = mock(JwtUtils.class);
        Authentication auth = mock(Authentication.class);

        // Simulé utilisateur
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "user",
                "user@mail.com",
                "password",
                List.of(() -> "ROLE_USER")
        );

        // Configuration des mocks
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateTokenFromUser(any())).thenReturn("mock.jwt.token");
        when(jwtUtils.getJwtTokenName()).thenReturn("jwt"); // ⚠️ C'est ici que tu empêches l'erreur

        // Contrôleur
        AuthController controller = new AuthController(null);
        controller.authenticationManager = authenticationManager;
        controller.jwtUtils = jwtUtils;

        // Requête
        LoginRequest loginRequest = new LoginRequest("user", "password");

        // Appel
        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        // Vérifications
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(UserInfoResponse.class, response.getBody());

        UserInfoResponse body = (UserInfoResponse) response.getBody();
        assertEquals("user", body.getUsername());
        assertEquals("user@mail.com", body.getEmail());
        assertEquals(List.of("ROLE_USER"), body.getRoles());
        assertEquals("mock.jwt.token", body.getToken());
    }
}
