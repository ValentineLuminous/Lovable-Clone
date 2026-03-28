FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["sh", "-c", "echo Waiting for DB... && sleep 15 && java -jar app.jar"]