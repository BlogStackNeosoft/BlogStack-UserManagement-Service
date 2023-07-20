FROM eclipse-temurin:20-alpine

WORKDIR /app

COPY target/employeeapp.jar /app/employeeapp.jar

ENTRYPOINT ["java", "-jar", "employeeapp.jar"]