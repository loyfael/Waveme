package fr.waveme.security.services;

import fr.waveme.crud.models.User;
import fr.waveme.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Références du dépôt `UserRepository` pour accéder aux données utilisateur
    @Autowired
    UserRepository userRepository;

    /**
     * Charge l'utilisateur par son nom d'utilisateur (`pseudo`) pour l'authentification.
     * Si l'utilisateur n'est pas trouvé, une exception `UsernameNotFoundException` est levée.
     *
     * @param pseudo Pseudo de l'utilisateur
     * @return Un objet `UserDetails` représentant l'utilisateur authentifié
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé avec le pseudo donné
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
        // Recherche l'utilisateur dans la base de données par son pseudo
        User user = userRepository.findByPseudo(pseudo)
                // Si aucun utilisateur n'est trouvé, lève une exception `UsernameNotFoundException`
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + pseudo));

        // Retourne une instance de `UserDetailsImpl` contenant les informations de l'utilisateur trouvé
        return UserDetailsImpl.build(user);
    }
}
