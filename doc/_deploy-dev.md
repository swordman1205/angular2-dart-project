# Build qurasense dev image and deploing it to google cloud

## Create service account in google console
 * Open google console in browser, go to project->apis->credentials->create credentials->service account key
 After creation press 'Manage ...' and select download, this file should be copied to image and exported:
 
    ```bash
    export GOOGLE_APPLICATION_CREDENTIALS=QurasenseDev.json
    ```

## Prepare image
 * Build static resources and copy it to user_api(at start you should cd to project root)
 
    ```bash
    ./generate_static_content.sh
    ```
 
 * Package application
  
    ```bash
    gradle assemble 
    ```
 
 
 * Build docker image
 
    ```bash 
    docker build -t gcr.io/qurasense-dev-1/qurasense-dev:v1 -f Dockerfile .
    ```
    
 * Run docker image and test image
 
    ```bash 
    docker run -it -p 80:8080 gcr.io/qurasense-dev-1/qurasense-dev:v1
    ```
  App will be available: http://${docker-machine ip}:8082
    
## Run in cloud
First we need to prepare the cloud for running our image

1. Login and set project 

    ```bash
    # login
    gcloud auth login
    
    # set default project
    gcloud config set project qurasense-dev-1 
    
    # test default project is qurasense-dev-1 
    gcloud config list project
 
    # set default zone
    gcloud config set compute/zone us-central1-c
    ```
    
2. Create cluster (note this need only be done once, also remember to change owner to your name)

    ```bash
    # create cluster(here we use 1 node of g1-small machine)
    gcloud container clusters create qurasense-cluster --num-nodes 1 --machine-type g1-small --labels=owner=lars
    
    # check cluster is running
    gcloud container clusters list
    ```
    
Next we need to push the docker image we create above and run it on our cluster
 
1. Use **qurasense-cluster** for running images 

    ```bash
    # set default cluster
    gcloud config set container/cluster qurasense-cluster
 
    # check default cluster is qurasense-cluster
    gcloud config list container/cluster
 
    # let kubernetes default to qurasense-cluster
    gcloud container clusters get-credentials qurasense-cluster
    kubectl proxy &
    ``` 
    
2. Push docker image for use in google cloud 

    ```bash
    # push image
    gcloud docker -- push gcr.io/qurasense-dev-1/qurasense-dev:v1
 
    # check it exists
    gcloud container images list --repository gcr.io/qurasense-dev-1/qurasense-dev:v1
    ```
3. Enable datastore and pubsub in browser console, 
    * press 'Enable ...' button in https://cloud.google.com/datastore/docs/activate, and select 'qurasense-dev' project
    * enable pubsub in https://console.cloud.google.com/apis/library/pubsub.googleapis.com/?q=pubsub&project=qurasense-dev-1
    
4. Start application in kubernetes managed cluster

    ```bash
    # start app
    kubectl run qurasense-dev --image=gcr.io/qurasense-dev-1/qurasense-dev:v1 --port=8082
   
    # check deployment is available (may take a few seconds)
    kubectl get deployments
 
    # check instances running
    kubectl get pods
    ```
5. Expose application to the outside

    ```bash
    # expose app by hosting it behind a public load balancer
    kubectl expose deployment qurasense-dev --type=LoadBalancer
 
    # get external api (may take some time, in output external ip will be available)
    kubectl get services
    ```
    
## Kompose + gcloud

    ```bash
    gcloud auth login
    gcloud auth application-default login
    kubectl proxy
    kompose convert
    kompose up
    ```