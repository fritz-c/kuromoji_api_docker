# kuromoji-api-docker

## Building and running
```sh
mvn clean install
mvn package
mvn dependency:copy-dependencies
java -cp "target/dependency/*:target/kuromoji_api_docker-1.0.jar" blue.hour.kuromoji_api_docker.App
```
