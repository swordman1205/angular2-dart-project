# README
This repository contains the backend, web and mobile application code for qurasense. The following modules exists

 * **app_facade** Application facade to the cloud API and generated models
 * **app_mobile** Mobile application for iPhone and Android,
 * **app_web** Web application for end users,
 * **cloud_doc** Project documentation including scripts used to set up Google cloud
 * **cloud_mock** Mock/emulator implementation of cloud services
 * **service_common** Common utilities and infrastructure for developing services
 * **service_gateway** API gateway which fronts the health, lab and user services
 * **service_health** API for health data and operations
 * **service_lab** API for lab data and operations
 * **service_user** API for user data and operations

# Software setup
 * Install Java 8 SDK and Gradle
 * Install Docker (including virtual machine for your OS)
 * Install [Google Cloud SDK](https://cloud.google.com/sdk/)
 * Install [IntelliJ Idea Ultimate](https://www.jetbrains.com/idea/download) 
 * Install [IntelliJ Dart plugin](https://www.dartlang.org/tools/jetbrains-plugin)
 * Install [IntelliJ Flutter plugin](https://flutter.io/intellij-ide/)
 * Install [IntelliJ Markdown navigator plugin](https://plugins.jetbrains.com/plugin/7896-markdown-navigator)
 * git clone https://gitlab.com/qurasense-dev/qurasense
 * Start Idea, click **Import Project** and select the **qurasense** project folder
 * IDEA will ask you to **import gradle projects**, click yes and import them
 * Open **Preferences -> Flutter** and point the **Flutter SDK path** to the folder where you installed flutter
 * Open the **pubspec.yml** in each of the  **app_facade**, **app_mobile** and **app_web** projects and click get dependencies in the **Pub actions pane**

## Updating software
 * Upgrade flutter: **flutter upgrade**
 * Upgrade Google cloud components: **gcloud components update**

# Web and App development
Web and app development can be done in multiple ways

1. Using mocked backend services.
2. Using local running services
3. Using services running in a Docker image
 
## Using mocked services
* Web development with mocked services 
    1. Open **app_web/lib/app_component.dart** and set **_mockBacked = true**.
    2. Open **app_web/web/index.html**, right click and click **Debug index.html**
    3. Then open the url http://localhost:8080 
* App development with mocked services 
    * TODO

## Using local running services
First go through the [Running services locally](#running-services-locally) 

* Web development with local services 
    1. 
    2. Open **user_web/web/index.html**, right click and click **Debug index.html** 
    3. Then open the url http://localhost:8080 
* App development with local services
    1. In IDEA Select **user_mobile** in the project pane and click **Tools -> FLutter -> Flutter Packages Get**
    2. Start device simulator
    3. Open **user_mobile/lib/main.dart** then right click and select **Run main.dart** 
  
## Using docker deployed services
TODO


# API Development
Our services can either be start each to run locally, this is suitable for debugging and when developing APIs, 
or they can be started from within a docker image

## Running services locally
1. We use Google data store and pub/sub, to do development sp we need to run emulators for these

    ```bash
    gcloud beta emulators datastore start --host-port localhost:8380 --data-dir emulator_data/datastore
    gcloud beta emulators pubsub start --host-port localhost:8381 --data-dir emulator_data/pubsub
    ```
    
2. In IDEA open **Tool Windows -> Run Dashboard** and start
    * **UserApiApplication** 
    * **HealthApiApplication** 
3. Test the  services
    * **user_api** runs on port 8080
        * Swagger file: http://localhost:8080/v2/api-docs
        * Swagger UI: http://localhost:8080/swagger-ui.html
        * REST resources (see swagger for full API)
            * http://localhost:8080/users/1 - get user by id
            * http://localhost:8080/users - get user list
    * **health_api** runs on port 8081
         * Swagger file: http://localhost:8081/v2/api-docs
         * Swagger UI: http://localhost:8081/swagger-ui.html
         * REST resources (see swagger for full API)
            * TODO
 
## Model generation from swagger files
We can generate models and distribute it with single gradle task. After this task swagger generated dart code will be copied to user_model
```shell
gradle distributeApi
``` 
Or we can generate models and after hands manipulations copy it to user_model. Dart code will be avaliable in `user_api/build/swagger/out`
```shell
cd user_api
gradle generateApi
``` 
 
# Cloud development

## Run locally using Docker 
* docker-machine start dev
* eval "$(docker-machine env dev)"
* gradle assemble
* docker build -t data-api:v1 .
* docker run -ti --rm -p 8080:8080 data-api:v1
   - Open at http://172.16.145.128:8080 (ip. gotten from **docker-machine ip dev**)
 
## Cloud notes

* Build image and test locally

    ```bash
    gradle assamble
    docker build -t gcr.io/qurasense-dev-1/user_health:v1 -f Dockerfile .
    docker run -it -P gcr.io/qurasense-dev-1/user_health:v1
    
    ```

* Set default zone
    3. 
    
* Update image
    1. kubectl set image deployment/hello-java hello-java=gcr.io/qurasense-174919/data-api:v2
 
 
# Resources
* https://cloud.google.com/stackdriver/
* https://gitlab.com/qurasense
* https://console.cloud.google.com/
* [Testing GCloud services using Emulator](https://github.com/GoogleCloudPlatform/google-cloud-java/blob/master/TESTING.md#testing-code-that-uses-datastore)
* http://cloud.spring.io/spring-cloud-gcp/ 
* https://medium.com/google-cloud/service-discovery-and-configuration-on-google-cloud-platform-spoiler-it-s-built-in-c741eef6fec2
* https://github.com/GoogleCloudPlatform/google-cloud-java/blob/master/TESTING.md#testing-code-that-uses-pubsub
* https://cloud.google.com/datastore/docs/activate
* https://medium.com/@DazWilkin/google-cloud-iap-and-gke-c773da56c3cf
* https://cloud.google.com/compute/docs/machine-types
* [Objectify - library for datastore](https://github.com/objectify/objectify)

## Urgent
* https://cloud.google.com/endpoints/docs/quickstart-endpoints
* http://eugeniopace.org/auth0/google/endpoints/jwt/2017/06/11/securing-google-cloud-endpoint-api-with-auth0.html
* http://eugeniopace.org/auth0/webtask/twilio/stoic/2017/07/16/using-twilio-and-webtask-to-become-a-better-stoic.html
* http://eugeniopace.org/auth0/webtask/twilio/sleep/mood/track/2017/10/15/tracking-sleep-mood-with-twilio-and-webtask.html
### HIPAA
* https://security.stackexchange.com/questions/96024/proper-implementation-of-hipaa-within-ios-app-with-several-factors
* https://cloud.google.com/dlp/

### Learning
* https://cloud.google.com/endpoints/docs/openapi/get-started-compute-engine-docker
* https://cloud.google.com/solutions/angularjs-cloud-endpoints-recipe-for-building-modern-web-applications
* https://medium.com/google-cloud/how-to-deploy-a-static-react-site-to-google-cloud-platform-55ff0bd0f509
* https://cloud.google.com/endpoints/docs/get-started-compute-engine-docker
* https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/ 
* http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
* https://www.packtpub.com/mapt/book/big_data_and_business_intelligence/9781785889936/1/ch01lvl1sec13/the-first-step-to-a-spark-program-in-java
* https://codelabs.developers.google.com/codelabs/cloud-springboot-kubernetes/index.html?index=..%2F..%2Findex#0
* https://stackoverflow.com/questions/33509865/running-dataproc-bigquery-example-on-local-machine
* https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-probes/
* https://cloud.google.com/endpoints/docs/openapi/get-started-container-engine
* Try using minikube https://www.brosinski.com/post/deploying-spring-boot-app-kubernetes/
* Level up in kubernetes https://docs.google.com/document/d/1E03-g0h3MgFlohXqPNYCjl7Pv_CA_l0zhD6K3MZ8O0M/edit

# URLs
 - trial.qurasense.com
 - test.qurasense.com(http://104.198.67.218/index.html)
 - dev.qurasense.com
 - https://www.html5rocks.com/en/tutorials/getusermedia/intro/
 
 - forgot email