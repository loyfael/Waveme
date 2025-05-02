package fr.waveme.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

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
        System.out.println("----------WAVEME BACKEND STARTED----------");
        System.out.println("URL de la datasource : " + datasourceUrl);
    }
}
