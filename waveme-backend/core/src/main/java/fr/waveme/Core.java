package fr.waveme;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class Core {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(Auth.class, Core.class)
                .run(args);
    }
}
