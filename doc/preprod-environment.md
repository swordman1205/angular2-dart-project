1. export import: https://cloud.google.com/datastore/docs/export-import-entities
2. create service account to access two projects https://stackoverflow.com/questions/35479025/cross-project-management-using-service-account
then update `kubernetes-preprod.yml`, and configure ci. Try to use test container(don`t create alias for preprod container registry)