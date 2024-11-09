package fr.waveme.backend.security;

import fr.waveme.backend.security.jwt.AuthEntryPointJwt;
import fr.waveme.backend.security.jwt.AuthTokenFilter;
import fr.waveme.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * Configuration de sécurité de l'application avec Spring Security.
 * Cette classe configure l'authentification, la gestion des sessions, les politiques CORS,
 * et définit un filtre pour gérer les jetons JWT.
 */
@Configuration
@EnableMethodSecurity // Active les annotations de sécurité sur les méthodes, comme @PreAuthorize
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Bean pour créer un filtre JWT personnalisé qui sera exécuté sur chaque requête.
     *
     * @return Instance d'`AuthTokenFilter`
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Fournisseur d'authentification basé sur la base de données, qui utilise le `UserDetailsServiceImpl`
     * pour charger les utilisateurs et `BCryptPasswordEncoder` pour encoder les mots de passe.
     *
     * @return Instance de `DaoAuthenticationProvider` configurée
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Utilise le service de gestion d'utilisateurs
        authProvider.setPasswordEncoder(passwordEncoder()); // Utilise BCrypt pour encoder les mots de passe
        return authProvider;
    }

    /**
     * Fournit un `AuthenticationManager`, utilisé par Spring Security pour la gestion des authentifications.
     *
     * @param authConfig Configuration d'authentification injectée
     * @return Instance d'`AuthenticationManager`
     * @throws Exception En cas de problème de configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Déclare l'encodeur de mot de passe à utiliser, ici `BCryptPasswordEncoder`.
     *
     * @return Instance de `PasswordEncoder` configurée
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la chaîne de filtres de sécurité avec les règles de gestion des sessions,
     * des CORS, des autorisations et des gestionnaires d'exception.
     *
     * @param http Objet de configuration `HttpSecurity`
     * @return Instance de `SecurityFilterChain`
     * @throws Exception En cas de problème de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Désactive la protection CSRF pour simplifier les requêtes API

                // Gestion des exceptions : utilise `AuthEntryPointJwt` pour gérer les accès non autorisés
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

                // Politique de session : défini comme sans état (STATELESS) pour les APIs REST
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuration des autorisations d'accès aux routes
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/register").permitAll() // Autorise l'accès à /register
                                .anyRequest().authenticated() // Nécessite une authentification pour toutes les autres routes
                );

        // Associe le fournisseur d'authentification à Spring Security
        http.authenticationProvider(authenticationProvider());

        // Ajoute le filtre JWT pour vérifier la présence d'un jeton avant que la requête atteigne les contrôleurs
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
