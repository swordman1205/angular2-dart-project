1. Create project in `Develop` folder
2. Add dev@qurasense.com as owner in IAM.
3. Ask Lars enable billing
4. Enable kubernetes engine
5. Create cluster
```
gcloud config set compute/zone us-central1-c
gcloud container clusters create qurasense-cluster --num-nodes 5 --machine-type n1-standard-1 --labels=owner=zufar --scopes=default,compute-rw,datastore,pubsub,storage-rw
```
6. Create ip `gcloud compute addresses create qurasense-cluster-ip --region us-central1`
```bash
# create address
gcloud compute addresses create qurasense-cluster-ip --region us-central1

# detect new ip value
gcloud compute addresses list
```
Then you need to update **.yml* with new ip. Find and replace *loadBalancerIP: 0.0.0.0* with new ip.
7. Enable appengine helloworld application to make datastore work