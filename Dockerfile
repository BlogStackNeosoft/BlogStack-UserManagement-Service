FROM eclipse-temurin:20-alpine

WORKDIR /app

COPY target/user-management-service.jar /app/user-management-service.jar

ENTRYPOINT ["java", "-jar", "user-management-service.jar"]