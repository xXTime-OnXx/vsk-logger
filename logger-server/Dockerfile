FROM eclipse-temurin:21
RUN mkdir /opt/app
WORKDIR /opt/app
COPY target/g04-loggerserver-1.0.0-SNAPSHOT-jar-with-dependencies.jar .
COPY src/main/docker/app.config .
EXPOSE 5555/tcp
CMD ["java", "-jar", "g04-loggerserver-1.0.0-SNAPSHOT-jar-with-dependencies.jar"]
