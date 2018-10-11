## Configure backups(https://cloud.google.com/datastore/docs/schedule-export)
1. create Cloud Storage bucket with Regional storage class, name backup-qurasense-test-1
2. configure iam:
    ```bash
    gcloud projects add-iam-policy-binding qurasense-test-1 --member serviceAccount:qurasense-test-1@appspot.gserviceaccount.com --role roles/datastore.importExportAdmin
    gsutil iam ch serviceAccount:qurasense-test-1@appspot.gserviceaccount.com:objectCreator gs://backup-qurasense-test-1
    ```
3. Deploy scheduled appengine application:
    ```bash
    gcloud app deploy app.yaml cron.yaml
    ```
3. After this every 24 hours (see cron.yaml to change period) backup will be saved to storage bucket
4. To restore, find version to restore(with browser) and restore:
    ```bash
    gcloud beta datastore import gs://backup-qurasense-test-1/20171129-0824/20171129-0824.overall_export_metadata
    ```
