FROM openjdk:8
COPY ./build/libs/*.jar ./service_user.jar
EXPOSE 8082/tcp
ENTRYPOINT ["java", "-Dqurasense.docker=true", "-jar", "./service_user.jar"]
