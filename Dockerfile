FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/aaalife-0.0.1-SNAPSHOT.jar claims.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/claims.jar"]
