# Base image con JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Cartella di lavoro
WORKDIR /app

# Creiamo le cartelle necessarie
RUN mkdir -p uploads/auto h2data

# Copiamo l'applicazione e il file di configurazione
COPY target/ioriocars-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties src/main/resources/application.properties

# Dichiarazione volume per H2 persistente
VOLUME /app/h2data

# Esponiamo la porta
EXPOSE 8080

# Entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
