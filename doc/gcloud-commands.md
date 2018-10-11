## Increase node pool size

    ```bash
    #get node pool name
    gcloud container node-pools list --cluster qurasense-cluster
    gcloud beta container clusters resize qurasense-cluster --node-pool [NODE_POOL] --size [SIZE]
    ```

## View current kubernetes version

    ```bash
    gcloud container get-server-config --zone us-central1-c
    ```