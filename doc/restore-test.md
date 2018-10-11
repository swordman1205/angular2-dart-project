1. Login and set project

    ```bash
    # login
    gcloud auth login

    # set default project
    gcloud config set project qurasense-test-1

    # set default zone
    gcloud config set compute/zone us-central1-c
    ```
2. Create cluster (note this need only be done once, also remember to change owner to your name)

    ```bash
    # create cluster
    gcloud container clusters create qurasense-test-cluster --num-nodes 3 --machine-type g1-small --labels=owner=zufar

    # check cluster is running
    gcloud container clusters list
    ```

3. Restore datastore from backup

    Obtain restore file path at: https://console.cloud.google.com/storage/browser/backup-qurasense-test-1?project=qurasense-test-1&organizationId=640664872803,
    then restore with desired *.overall_export_metadata file, for example:

    ```bash
    gcloud beta datastore import gs://backup-qurasense-test-1/20171129-0824/20171129-0824.overall_export_metadata
    ```

4. Get new static ip to access cluster

    ```bash
    # create address
    gcloud compute addresses create qurasense-test-cluster-ip --region us-central1

    # detect new ip value
    gcloud compute addresses list
    ```
    Then you need to update *kubernetes-test.yml* with new ip. Find and replace *loadBalancerIP: 104.198.67.218* with new ip.

5. Launch kubernetes proxy

    ```bash
    # let kubernetes default to qurasense-test-cluster
    gcloud container clusters get-credentials qurasense-test-cluster
    kubectl proxy &
    ```

6. Deploy images from gcr to new cluster

    ```bash
    # let kubernetes default to qurasense-test-cluster
    kubectl delete secret google-key --ignore-not-found
    # file with key should have name 'key.json'
    copy QurasenseTest.json key.json
    kubectl create secret generic google-key --from-file=key.json
    kubectl apply -f kubernetes-test.yml
    ```

6. Check results

    Wait till all pods will be in "Running" STATUS
    ```bash
    kubectl get pods
    ```
    Wait till gateway service obtain static EXTERNAL-IP
    ```bash
    kubectl get services
    ```
    Check app is running on EXTERNAL-IP. For example http://35.184.97.254/index.html

7. Read pod logs to detect troubles

    ```bash
    # Get pod name
    kubectl get pods
    # view log for pod
    kubectl logs userdeployment-1973620317-bs0n5
    ```

8. Delete cluster and release ip (optional)

    ```bash
    # delete cluster
    gcloud container clusters delete qurasense-test-cluster
    # release static ip, reserved ip is billed
    gcloud compute addresses delete qurasense-test-cluster-ip
    ```
