# Base image con JDK 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

RUN mkdir -p uploads/auto db

COPY target/ioriocars-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties src/main/resources/application.properties

VOLUME /app/db

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
