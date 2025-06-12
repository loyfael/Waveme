package fr.waveme.backend.auth.crud.controller;

import fr.waveme.backend.auth.crud.models.enumerators.ERole;
import fr.waveme.backend.auth.crud.models.Role;
import fr.waveme.backend.auth.crud.models.User;
import fr.waveme.backend.auth.crud.repository.RoleRepository;
import fr.waveme.backend.auth.crud.repository.UserRepository;
import fr.waveme.backend.auth.payload.request.LoginRequest;
import fr.waveme.backend.auth.payload.request.RegisterRequest;
import fr.waveme.backend.auth.payload.response.MessageResponse;
import fr.waveme.backend.auth.payload.response.UserInfoResponse;
import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.security.services.UserDetailsImpl;
import fr.waveme.backend.social.crud.dto.UserProfileDto;
import fr.waveme.backend.social.crud.service.UserProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AuthController handles user authentication and registration.
 * It provides endpoints for user registration, login, and logout.
 * This controller uses Spring Security for authentication management
 * and interacts with the UserRepository and RoleRepository for user data.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private final UserProfileService userProfileService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  public AuthController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  /**
     * Endpoint to register a new user.
     *
     * @param signUpRequest The registration request containing user details.
     * @return ResponseEntity with success or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByPseudo(signUpRequest.getPseudo())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Création Auth
        User user = new User();
        user.setPseudo(signUpRequest.getPseudo());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        // Création directe du profil Social dans MongoDB via le service
        UserProfileDto socialProfile = new UserProfileDto();
        socialProfile.setId(String.valueOf(savedUser.getId())); // Correspond à l'ID de l'utilisateur
        socialProfile.setAuthId(savedUser.getId());
        socialProfile.setPseudo(savedUser.getPseudo());
        socialProfile.setEmail(savedUser.getEmail());
        socialProfile.setProfileImg(null); // Pas d'image de profil par défaut
        socialProfile.setTotalPosts(0);
        socialProfile.setTotalComments(0);
        socialProfile.setTotalUpVote(0);
        socialProfile.setCreatedAt(LocalDateTime.now());
        socialProfile.setUpdatedAt(LocalDateTime.now());

        try {
            userProfileService.save(socialProfile);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du profil", e);
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    /**
     * Endpoint to authenticate a user.
     *
     * @param loginRequest The login request containing username and password.
     * @return ResponseEntity with user information and JWT token if authentication is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getPseudo(), loginRequest.getPassword())
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtils.generateTokenFromUser(userDetails); // méthode déjà faite dans ta JwtUtils
        ResponseCookie jwt = ResponseCookie.from(jwtUtils.getJwtTokenName(), token)
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();


        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, token)
                .body(new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        token
                ));
    }

    /**
     * Endpoint to log out a user.
     *
     * @return ResponseEntity with a message indicating successful logout.
     */
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwt();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
