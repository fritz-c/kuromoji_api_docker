FROM openjdk:8-alpine

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/api/run.jar

CMD ["/usr/bin/java", "-cp", "/opt/api/run.jar", "com.example.kuromoji_api_docker.App"]
