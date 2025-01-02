package fr.waveme.backend.security.jwt;

import fr.waveme.backend.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // on recupere le JWT de la requete
            String jwt = parseJwt(request);
            // s'il est present et valide
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // on recup son username (on pourrait recup son ID si besoin)
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                // on utilise la classe qui va bien pour recup l'user associé
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // On créé un UsernamePasswordAuthentificationToken qui va verifier directement la validité du user
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                // on construit l'objet qui représente l'authentification dans spring secu
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // On le place dans le contexte (sorte de session) pour l'utiliser plus loin dans le code (si on est loggé?)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: ", e);
        }
        // On renvoi la requête.
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }
}
