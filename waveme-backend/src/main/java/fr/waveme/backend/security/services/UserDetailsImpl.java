package fr.waveme.backend.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.waveme.backend.crud.models.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * `UserDetailsImpl` implémente `UserDetails` pour représenter un utilisateur authentifié dans
 * le contexte de Spring Security. Elle stocke les informations de l'utilisateur et ses autorisations.
 */
public class UserDetailsImpl implements UserDetails {

    // Identifiant unique de l'utilisateur
    @Setter
    @Getter
    private Long id;

    // Nom d'utilisateur (pseudo)
    @Setter
    private String username;

    // Adresse email de l'utilisateur
    @Setter
    @Getter
    private String email;

    // Mot de passe de l'utilisateur, annoté avec @JsonIgnore pour être exclu des sérialisations JSON
    @JsonIgnore
    private String password;

    // Collection des autorisations de l'utilisateur (ex. : rôles), nécessaires pour Spring Security
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructeur de `UserDetailsImpl` pour initialiser toutes les informations d'utilisateur.
     *
     * @param id          Identifiant de l'utilisateur
     * @param username    Nom d'utilisateur (pseudo)
     * @param email       Adresse email de l'utilisateur
     * @param password    Mot de passe de l'utilisateur
     * @param authorities Collection des autorisations de l'utilisateur
     */
    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Méthode de construction `build` pour convertir un objet `User` en `UserDetailsImpl`.
     * Elle convertit les rôles de l'utilisateur en objets `SimpleGrantedAuthority` pour Spring Security.
     *
     * @param user L'objet `User` à convertir
     * @return Un nouvel objet `UserDetailsImpl` contenant les informations de l'utilisateur et ses autorisations
     */
    public static UserDetailsImpl build(User user) {
        // Convertit les rôles en `SimpleGrantedAuthority` pour chaque rôle
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(), user.getPseudo(), user.getEmail(), user.getPassword(), authorities);
    }

    /**
     * Renvoie la collection des autorisations de l'utilisateur.
     * @return Collection des autorisations de l'utilisateur
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Renvoie le mot de passe de l'utilisateur pour les opérations d'authentification.
     * @return Mot de passe de l'utilisateur
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Renvoie le nom d'utilisateur (pseudo) pour les opérations d'authentification.
     * @return Nom d'utilisateur
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indique si le compte de l'utilisateur est expiré. (Toujours `true` ici)
     * @return `true` car le compte est considéré non expiré par défaut
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indique si le compte de l'utilisateur est verrouillé. (Toujours `true` ici)
     * @return `true` car le compte est considéré non verrouillé par défaut
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Indique si les informations d'authentification de l'utilisateur sont expirées. (Toujours `true` ici)
     * @return `true` car les informations d'authentification sont considérées non expirées par défaut
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Indique si le compte de l'utilisateur est activé. (Toujours `true` ici)
     * @return `true` car le compte est considéré activé par défaut
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
