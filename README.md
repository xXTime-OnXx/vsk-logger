# Logger

Implementation des Loggers der Gruppe g04 im Modul VSK FS24.

# Docker Image
## Build
Um ein Docker Image zu erstellen, kann der maven build mit dem profile `docker-image`ausgeführt werden.
```shell
mvn clean install -P docker-image
```
## Start
Das erstellte Docker Image kann mit folgendem command ausgeführt werden:
```shell
mvn docker:start -P docker-image
```