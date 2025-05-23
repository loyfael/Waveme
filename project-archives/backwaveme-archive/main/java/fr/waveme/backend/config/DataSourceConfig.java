package fr.waveme.backend.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DataSourceConfig implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.access-key}")
    private String minioAccessKey;

    @Value("${minio.secret-key}")
    private String minioSecretKey;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Waveme Backend is starting...");

        // Vérification de la connexion à la base de données
        try (Connection connection = DriverManager.getConnection(
                datasourceUrl,
                datasourceUsername,
                datasourcePassword)) {
            System.out.println("\u001B[32m✓ Database connection successful\u001B[0m");
        } catch (SQLException e) {
            System.err.println("\u001B[31m✗ Failed to connect to database: " + e.getMessage() + "\u001B[0m");
            throw new RuntimeException("Database connection failed", e);
        }

        // Vérification de la connexion à Minio
        try (MinioClient minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build()) {

            // Vérifier si Minio est accessible
            minioClient.listBuckets();
            System.out.println("\u001B[32m✓ Minio connection successful\u001B[0m");
        } catch (Exception e) {
            System.err.println("\u001B[31m✗ Failed to connect to Minio: " + e.getMessage() + "\u001B[0m");
            throw new RuntimeException("Minio connection failed", e);
        }

        System.out.print("\u001B[32m");
        System.out.println("----------WAVEME BACKEND STARTED----------");
        System.out.print("\u001B[0m");
    }
}