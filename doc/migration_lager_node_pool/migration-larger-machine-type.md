## Migration to larger machine type without downtime(https://cloud.google.com/kubernetes-engine/docs/tutorials/migrating-node-pool)
In example we migrate from g1-small to n1-standard-1
1. Lanuch kubectl proxy and view current node pool
    ```bash
    gcloud container clusters get-credentials qurasense-cluster
    kubectl proxy
    gcloud container node-pools list --cluster qurasense-cluster
    ```
2. Create larger pool(this take minute or two), and check it was created
    ```bash
    gcloud container node-pools create larger-pool --cluster qurasense-cluster --machine-type=n1-standard-1 --num-nodes=4
    gcloud container node-pools list --cluster qurasense-cluster
    ```
3. Ensure larger nodes are running
    ```bash
    kubectl get nodes
    ```
4. Cordon the existing node pool(at windows launch kordon-nodes.bat)
    ```bash
    #list nodes to cordon
    kubectl get nodes -l cloud.google.com/gke-nodepool=default-pool
    #cordon bash
    for node in $(kubectl get nodes -l cloud.google.com/gke-nodepool=default-pool -o=name); do kubectl cordon "$node"; done
    ```
5. Ensure  nodes have SchedulingDisabled status in the node list
    ```bash
    kubectl get nodes
    ```
6. Drain nodes(take some time)
    ```bash
    for node in $(kubectl get nodes -l cloud.google.com/gke-nodepool=default-pool -o=name); do kubectl drain --force --ignore-daemonsets "$node"; done
    ```
7. Ensure that the Pods are now running on the larger-pool nodes:
    ```bash
    kubectl get pods -o=wide
    ```
8. Delete the old node pool(take some time), and check it was deleted
    ```bash
    gcloud container node-pools delete default-pool --cluster qurasense-cluster
    gcloud container node-pools list --cluster qurasense-cluster
    ```
