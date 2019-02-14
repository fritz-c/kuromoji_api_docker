#!/usr/bin/env bash

set -e
set -x

java -cp "target/dependency/*:$(echo target/kuromoji_api_docker-*.jar)" com.example.kuromoji_api_docker.App
