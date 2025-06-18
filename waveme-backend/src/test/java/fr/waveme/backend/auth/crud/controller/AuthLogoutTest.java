package fr.waveme.backend.auth.crud.controller;

import fr.waveme.backend.auth.payload.response.MessageResponse;
import fr.waveme.backend.security.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthLogoutTest {
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final AuthController controller;

    public AuthLogoutTest() {
        controller = new AuthController(null);
        controller.jwtUtils = jwtUtils;
    }

    @Test
    void logout_shouldReturnCleanCookieAndMessage() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();

        when(jwtUtils.getCleanJwt()).thenReturn(cookie);

        ResponseEntity<?> response = controller.logoutUser();

        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertTrue(((MessageResponse) response.getBody()).getMessage().contains("signed out"));
    }
}
