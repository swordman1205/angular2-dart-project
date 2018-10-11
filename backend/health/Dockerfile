FROM openjdk:8
COPY ./build/libs/*.jar ./service_health.jar
EXPOSE 8081/tcp
ENTRYPOINT ["java", "-Dqurasense.docker=true", "-jar", "./service_health.jar"]
