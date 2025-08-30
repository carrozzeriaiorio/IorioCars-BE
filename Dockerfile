# Base image con JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Cartella di lavoro
WORKDIR /app

# Creiamo la cartella per gli upload (se serve)
RUN mkdir -p uploads/auto

# Copiamo l'applicazione e il file di configurazione
COPY target/ioriocars-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties src/main/resources/application.properties

# Esponiamo la porta dell'app Spring Boot
EXPOSE 8080

# Entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
