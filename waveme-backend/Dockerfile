# Step 1: Build the application using Maven
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Step 2: Create the runtime image
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 9998

# Set the entry point for the container
ENTRYPOINT ["java", "-Dspring.config.location=classpath:/application.properties", "-jar", "app.jar"]
