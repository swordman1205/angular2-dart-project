# cloud_mock
This project contains mocking Docker script for running our application outside of cloud environment. 

# run emulator docker images

To run mock app with docker(with emulators), user should execute this commands:

    ./gradlew :user_web:compileDart :gateway:assemble :user_api:assemble :health_api:assemble
     docker-compose build
     docker-compuse up

Web could be accessed at http://172.16.145.128:8080 (ip. gotten from **docker-machine ip dev**, on linux and windows localhost)
