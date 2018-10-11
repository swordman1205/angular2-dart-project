1. Install helm at gcloud side(from gcloud web console)

```
gcloud container clusters get-credentials qurasense-test-cluster
wget https://storage.googleapis.com/kubernetes-helm/helm-v2.8.2-linux-amd64.tar.gz
kubectl create serviceaccount tiller --namespace kube-system
kubectl create clusterrolebinding tiller-binding --clusterrole=cluster-admin --serviceaccount kube-system:tiller
helm init --service-account tiller
```

2. Install cert/manager to get lets encrypt ssl certificate

```
gcloud container clusters get-credentials qurasense-test-cluster
helm install --set ingressShim.extraArgs={--default-issuer-name=letsencrypt-test,--default-issuer-kind=ClusterIssuer} --set rbac.create=false --name cert-manager --namespace kube-system stable/cert-manager
```

3. Add ingress service description and link it with exposed service(see `kubernetes-prod.yml`)

4. Obtain certificate and check ingress ready
```
#enable issuer
helm upgrade cert-manager stable/cert-manager --namespace kube-system --set ingressShim.extraArgs={--default-issuer-name=letsencrypt-test,--default-issuer-kind=ClusterIssuer}
#check cerificate obtained
kubectl get certificates --all-namespaces
#check certificate status, usualy 10 min, event should contain: "Certificate scheduled for renewal in 1438 hours"
kubectl describe certificate qurasense-test-tls
#check ingress ready, i waited about 15 min after event "Successfully created Certificate "qurasense-test-tls""
kubectl describe ing
```