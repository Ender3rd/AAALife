FROM eclipse-temurin:26_35-jre-alpine

WORKDIR /opt/app

COPY target/aaalife-0.0.1-SNAPSHOT.jar claims.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/claims.jar"]
