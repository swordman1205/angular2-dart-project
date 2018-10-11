Assume we have stable version at test project and want to deploy latest version to production cluster.

# Deploy preproduction
1. In last [pipeline](https://gitlab.com/qurasense-dev/qurasense/pipelines) execute `push_preproduction_images` job from `production_docker_images` stage.
2. Then execute `k8s-deploy-preproducation` job from `deploy` stage.

After second success - preproduction should be available in about 2-5 mins.

# Deploy production
1. In last [pipeline](https://gitlab.com/qurasense-dev/qurasense/pipelines) execute `push_production_images` job from `production_docker_images` stage.
2. Then execute `k8s-deploy-producation` job from `deploy` stage.

After second success - trial should be available in about 2-5 mins.