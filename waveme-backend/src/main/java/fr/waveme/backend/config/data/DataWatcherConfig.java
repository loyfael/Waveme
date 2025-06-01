package fr.waveme.backend.config.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "fr.waveme.backend.social.crud.repository")
@EnableJpaRepositories(basePackages = "fr.waveme.backend.auth.crud.repository")
public class DataWatcherConfig {
}