## Build image

```
cd C:\Work\qurasense\qurasense\testing\chrome_headless
docker build -t registry.gitlab.com/qurasense-dev/qurasense/chrome_gradle .
```

## Execute tests inside docker container

```
docker run -v C:\Work\qurasense\qurasense:/qurasense -it --security-opt seccomp:unconfined registry.gitlab.com/qurasense-dev/qurasense/chrome_gradle /bin/bash
cd /qurasense/testing/gui_tests/
gradle test
```

## Check tests status

At first find docker container id with `docker ps`.
Then copy test report to host with `docker cp $containerId:/qurasense/testing/gui_tests C:/Work/qurasense/tmp/gradleBuildFail4`.
Then view in browser _C:\Work\qurasense\tmp\gradleBuildFail4\build\reports\tests\test\index.html_