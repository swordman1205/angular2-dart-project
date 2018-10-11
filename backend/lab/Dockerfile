FROM openjdk:8
COPY ./build/libs/*.jar ./service_lab.jar
EXPOSE 8083/tcp
ENTRYPOINT ["java", "-Dqurasense.docker=true", "-jar", "./service_lab.jar"]
