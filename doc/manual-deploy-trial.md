Assume we have stable version at test project and want to deploy this version to production cluster. Here is command list:

1. Login to test project and pull images

    ```bash
    # login
    gcloud auth login

    # set default project
    gcloud config set project qurasense-test-1

    # set default zone
    gcloud config set compute/zone us-central1-c

    # pull stable images to local
    gcloud docker -- pull gcr.io/qurasense-test-1/service_gateway:v1
    gcloud docker -- pull gcr.io/qurasense-test-1/service_user:v1
    gcloud docker -- pull gcr.io/qurasense-test-1/service_health:v1
    ```

2. Set prod project and deploy stable images

    ```bash

    # set default project
    gcloud config set project qurasense-prod-1

    # tag stable images
    docker tag gcr.io/qurasense-test-1/service_gateway:v1 gcr.io/qurasense-prod-1/service_gateway:v1
    docker tag gcr.io/qurasense-test-1/service_user:v1 gcr.io/qurasense-prod-1/service_user:v1
    docker tag gcr.io/qurasense-test-1/service_health:v1 gcr.io/qurasense-prod-1/service_health:v1

    # push stable images
    gcloud docker -- push gcr.io/qurasense-prod-1/service_gateway:v1
    gcloud docker -- push gcr.io/qurasense-prod-1/service_user:v1
    gcloud docker -- push gcr.io/qurasense-prod-1/service_health:v1

    ```

3. Launch kubernetes proxy

    ```bash
    # let kubernetes default to qurasense-cluster
    gcloud container clusters get-credentials qurasense-cluster
    kubectl proxy &
    ```

4. Deploy images from gcr to new cluster

    ```bash
    # delete old secret
    kubectl delete secret google-key --ignore-not-found
    # file with key should have name 'key.json'
    copy QurasenseProd.json key.json
    kubectl create secret generic google-key --from-file=key.json
    kubectl apply -f kubernetes-prod.yml
    ```

5. Check results

    Wait till all pods will be in "Running" STATUS
    ```bash
    kubectl get pods
    ```
    Wait till gateway service obtain static EXTERNAL-IP
    ```bash
    kubectl get services
    ```
    Check app is running on EXTERNAL-IP: http://35.227.211.100/index.html

6. Read pod logs to detect troubles

    ```bash
    # Get pod name
    kubectl get pods
    # view log for pod
    kubectl logs userdeployment-1973620317-bs0n5
    ```
