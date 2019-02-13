FROM openjdk:8-jre

WORKDIR /opt/api

COPY . .

RUN mvn install

CMD ["bash"]
