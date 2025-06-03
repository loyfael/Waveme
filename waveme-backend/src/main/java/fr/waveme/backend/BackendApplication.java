package fr.waveme.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BackendApplication is the main entry point for the Spring Boot application.
 * It starts the application and initializes the Spring context.
 */
@SpringBootApplication
public class BackendApplication {
	public static void main(String[] args)
	{
		SpringApplication.run(BackendApplication.class, args);
	}
}
