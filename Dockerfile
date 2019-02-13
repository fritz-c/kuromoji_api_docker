FROM openjdk:8-jre

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/api/run.jar

ENTRYPOINT ["/usr/bin/java", "-cp", "/opt/api/run.jar", "blue.hour.kuromoji_api_docker.App"]
