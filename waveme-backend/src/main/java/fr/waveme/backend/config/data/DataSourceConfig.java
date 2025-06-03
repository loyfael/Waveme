package fr.waveme.backend.config.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DataSourceConfig is a Spring component that implements CommandLineRunner.
 * It is responsible for initializing and verifying connections to various data sources
 * such as PostgreSQL, MongoDB, and Minio when the application starts.
 */
@Component
public class DataSourceConfig implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String mongoDatabaseName;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.access-key}")
    private String minioAccessKey;

    @Value("${minio.secret-key}")
    private String minioSecretKey;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Waveme Backend is starting...");

        // Vérification de la connexion à la base de données relationnelle (PostgreSQL)
        try (Connection connection = DriverManager.getConnection(
                datasourceUrl,
                datasourceUsername,
                datasourcePassword)) {
            System.out.println("\u001B[32m✓ PostgreSQL database connection successful !\u001B[0m");
        } catch (SQLException e) {
            System.err.println("\u001B[31m✗ Failed to connect to PostgreSQL database: " + e.getMessage() + "\u001B[0m");
            throw new RuntimeException("Database connection failed", e);
        }

        // Vérification de la connexion à MongoDB
        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase(mongoDatabaseName);
            database.runCommand(new org.bson.Document("ping", 1));
            System.out.println("\u001B[32m✓ MongoDB connection successful\u001B[0m");
        } catch (Exception e) {
            System.err.println("\u001B[31m✗ Failed to connect to MongoDB: " + e.getMessage() + "\u001B[0m");
            throw new RuntimeException("MongoDB connection failed", e);
        }

        // Vérification de la connexion à Minio
        try (MinioClient minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build()) {

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