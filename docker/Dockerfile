FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ../target/store-1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
