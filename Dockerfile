# Base image con JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Cartella di lavoro
WORKDIR /app

# Crea cartella per immagini persistenti
RUN mkdir -p uploads/auto

# Copia il jar buildato
COPY target/ioriocars-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta 8080
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java","-jar","app.jar"]
