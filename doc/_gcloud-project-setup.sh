#!/usr/bin/env bash

##################################################################
# This script documents our cloud organization and project setup #
##################################################################

# login
gcloud auth login

## get billing account
gcloud alpha billing accounts list

# get organization id (640664872803)
gcloud organizations list | grep qurasense.com | awk '{print $2}'

# create platform folder in organization
gcloud alpha resource-manager folders create --display-name="Platform" --organization=640664872803

# get platform folder id (480024715013)
gcloud alpha resource-manager folders list --organization=640664872803 | grep Platform | awk '{print $3}'

# Create qurasense-dev project in platform folder
gcloud projects create qurasense-dev-1 --name="Qurasense Dev" --labels=owner=lars,environment=dev --folder 480024715013
gcloud alpha billing accounts projects link qurasense-dev-1 --account-id=
# Let dev group administer qurasense-dev project
gcloud projects add-iam-policy-binding qurasense-dev-1 --member="group:dev@qurasense.com" --role="roles/owner"

# Create qurasense-test project in platform folder
gcloud projects create qurasense-test-1 --name="Qurasense Test" --labels=owner=lars,environment=test --folder 480024715013
gcloud alpha billing accounts projects link qurasense-test-1 --billing-account=
# Let dev group administer qurasense-test project
gcloud projects add-iam-policy-binding qurasense-test-1 --member="group:dev@qurasense.com" --role="roles/owner"

# Create qurasense-prod project in platform folder
gcloud projects create qurasense-prod-1 --name="Qurasense Prod" --labels=owner=lars,environment=prod --folder 480024715013
gcloud alpha billing accounts projects link qurasense-prod-1 --billing-account=

## set current project
gcloud config set project qurasense-dev-1


