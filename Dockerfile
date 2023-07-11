FROM eclipse-temurin:20-alpine

WORKDIR /app

COPY target/blogstackiamservice.jar /app/blogstackiamservice.jar

ENTRYPOINT ["java", "-jar", "blogstackiamservice.jar"]