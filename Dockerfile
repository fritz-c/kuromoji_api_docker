# ------ Bundle build image --------
FROM maven:3-jdk-8-alpine AS build-env

WORKDIR /build

# copy the Project Object Model file
COPY ./pom.xml ./pom.xml

# fetch all dependencies
RUN mvn de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

COPY ./src ./src

RUN mvn clean package -Dbuild=full && cp ./target/uber-*.jar ./run.jar

# ------ Bundle run image --------
FROM openjdk:8-alpine

COPY --from=build-env /build/run.jar /app/run.jar

CMD ["/usr/bin/java", "-cp", "/app/run.jar", "com.example.kuromoji_api_docker.App"]
