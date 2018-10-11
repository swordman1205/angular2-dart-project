## Interactive bash in container with mounted directory

    ```cmd
    docker run -v C:\Work\qurasense\qurasense\user_web:/some -it google/dart /bin/bash
    ```

## Copy from container to host

    ```cmd
    docker cp <containerId>:/file/path/within/container /host/path/target
    ```
