package fr.waveme.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSourceConfig implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("URL de la datasource : " + datasourceUrl);
        System.out.println("Nom d'utilisateur de la datasource : " + datasourceUsername);
        // Pour des raisons de sécurité, évitez d'afficher le mot de passe en production
        System.out.println("Mot de passe de la datasource : " + datasourcePassword);
    }
}
