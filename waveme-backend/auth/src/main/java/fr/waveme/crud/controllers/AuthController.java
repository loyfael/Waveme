package fr.waveme.crud.controllers;

import fr.waveme.crud.models.Role;
import fr.waveme.crud.models.User;
import fr.waveme.crud.models.enumerators.ERole;
import fr.waveme.crud.repository.RoleRepository;
import fr.waveme.crud.repository.UserRepository;
import fr.waveme.payload.request.LoginRequest;
import fr.waveme.payload.request.RegisterRequest;
import fr.waveme.payload.response.MessageResponse;
import fr.waveme.payload.response.UserInfoResponse;
import fr.waveme.security.jwt.AuthJwtUtils;
import fr.waveme.security.services.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    AuthJwtUtils authJwtUtils;

    @PostConstruct
    public void init() {
        System.out.println("✅ AuthController chargé !");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {

        System.out.println("✅ registerUser invoked");
        // Si l'utilisateur existe déjà
        if (userRepository.existsByPseudo(signUpRequest.getPseudo())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        // Si le mail est déjà utilisé.
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setPseudo(signUpRequest.getPseudo());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                loginRequest.getPseudo(), loginRequest.getPassword())
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                String token = authJwtUtils.generateTokenFromUser(userDetails); // méthode déjà faite dans ta JwtUtils
                ResponseCookie jwt = ResponseCookie.from(authJwtUtils.getJwtCookieName(), token)
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
        } catch (Exception e) {
                System.out.println("❌ Authentication failed: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = authJwtUtils.getCleanJwt();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}
