FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/stayhub_reservation-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
EXPOSE 9091

ENTRYPOINT ["java", "-jar","app.jar"]

