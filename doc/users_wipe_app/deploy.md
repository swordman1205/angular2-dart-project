## Deploy user wipe application
1. To deploy
    ```bash
    gcloud app deploy app.yaml
    ```
2. View logs
    ```bash
    gcloud app logs tail -s cloud-datastore-users-wipe
    ```
