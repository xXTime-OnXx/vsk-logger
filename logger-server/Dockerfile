FROM eclipse-temurin:21

RUN mkdir /opt/app
WORKDIR /opt/app

COPY target/g04-logger-server.jar .
COPY src/main/docker/app.config .

EXPOSE 5555/tcp

CMD ["java", "-jar", "g04-logger-server.jar"]
