# Étape 1 : Construire l'application
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Exécuter l'application
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copier le JAR depuis l'étape de construction
COPY --from=build /app/target/*.jar app.jar

# Exposer le port de l'application
EXPOSE 9080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
