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

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        logger.info("Processing request for path: {}", path);

        // Ignorer les routes publiques comme /api/auth/register
        if (path.equals("/api/auth/register") || path.equals("/register")) {
            logger.info("Skipping AuthTokenFilter for public path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        if (path.equals("/api/auth/login") || path.equals("/login")) {
            logger.info("Skipping AuthTokenFilter for public path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);
            logger.info("JWT extracted: {}", jwt != null ? "Found" : "Not found");

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.info("JWT is valid. Username extracted: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("User authenticated: {}", username);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            logger.info("JWT found in Authorization header");
            return headerAuth.substring(7);
        }

        logger.info("No JWT in Authorization header, checking cookies");
        return jwtUtils.getJwtFromCookies(request);
    }
}
