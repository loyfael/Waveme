package fr.waveme.backend.auth.crud.controller;

import fr.waveme.backend.auth.crud.models.Role;
import fr.waveme.backend.auth.crud.models.User;
import fr.waveme.backend.auth.crud.models.enumerators.ERole;
import fr.waveme.backend.auth.crud.repository.RoleRepository;
import fr.waveme.backend.auth.crud.repository.UserRepository;
import fr.waveme.backend.auth.payload.request.RegisterRequest;
import fr.waveme.backend.auth.payload.response.MessageResponse;
import fr.waveme.backend.social.crud.dto.UserProfileDto;
import fr.waveme.backend.social.crud.service.UserProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthRegisterTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final UserProfileService userProfileService = mock(UserProfileService.class);

    private final AuthController controller;

    public AuthRegisterTest() {
        controller = new AuthController(userProfileService);
        controller.userRepository = userRepository;
        controller.roleRepository = roleRepository;
        controller.encoder = encoder;
    }

    @Test
    void registerUser_shouldReturnErrorIfUsernameExists() {
        RegisterRequest request = new RegisterRequest("existingUser", "email@mail.com", "password");
        when(userRepository.existsByPseudo("existingUser")).thenReturn(true);

        ResponseEntity<?> response = controller.registerUser(request);

        assertEquals(400, response.getStatusCode().value());
        assert response.getBody() != null;
        assertTrue(((MessageResponse) response.getBody()).getMessage().contains("Username is already taken"));
    }

    @Test
    void registerUser_shouldReturnErrorIfEmailExists() {
        RegisterRequest request = new RegisterRequest("user", "existing@mail.com", "password");
        when(userRepository.existsByPseudo("user")).thenReturn(false);
        when(userRepository.existsByEmail("existing@mail.com")).thenReturn(true);

        ResponseEntity<?> response = controller.registerUser(request);

        assertEquals(400, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertTrue(((MessageResponse) response.getBody()).getMessage().contains("Email is already in use"));
    }

    @Test
    void registerUser_shouldRegisterSuccessfully() {
        RegisterRequest request = new RegisterRequest("user", "user@mail.com", "pass");

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);

        when(userRepository.existsByPseudo("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@mail.com")).thenReturn(false);
        when(encoder.encode("pass")).thenReturn("encoded");
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        ResponseEntity<?> response = controller.registerUser(request);

        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
        verify(userProfileService).save(any(UserProfileDto.class));
    }
}
