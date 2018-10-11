FROM openjdk:8
COPY ./build/libs/*.jar ./service_gateway.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-Dqurasense.docker=true", "-jar", "./service_gateway.jar"]
