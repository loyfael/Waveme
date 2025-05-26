package fr.waveme;

import fr.waveme.storage.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "fr.waveme")
@EnableConfigurationProperties(MinioProperties.class)
public class Core {
    public static void main(String[] args) {
        SpringApplication.run(Core.class, args);
    }
}
