# Utiliser l'image openjdk:22 comme base
FROM openjdk:22-jdk

# Définir le répertoire de travail
WORKDIR /waveme-backend

# Copier le fichier JAR de l'application dans le conteneur
COPY target/myapp.jar /app/myapp.jar

# Exposer le port de l'application
EXPOSE 8080

# Commande pour exécuter l'application
CMD ["java", "-jar", "/app/waveme.jar"]
