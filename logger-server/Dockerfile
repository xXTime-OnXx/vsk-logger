FROM eclipse-temurin:21
RUN mkdir /opt/app
COPY target/g04-loggerserver-1.0.0-SNAPSHOT-jar-with-dependencies.jar /opt/app
EXPOSE 5555/tcp
CMD ["java", "-jar", "/opt/app/g04-loggerserver-1.0.0-SNAPSHOT-jar-with-dependencies.jar"]
