FROM openjdk:8
COPY ./build/libs/*.jar ./service_communication.jar
ENTRYPOINT ["java", "-Dqurasense.docker=true", "-jar", "./service_communication.jar"]
