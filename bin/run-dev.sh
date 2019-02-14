#!/usr/bin/env bash

set -e

PROJECT_DIR="$( dirname "$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )" )"
JAR_FILE="$(echo $PROJECT_DIR/target/kuromoji_api_docker-*.jar)"

set -x

java -cp "$PROJECT_DIR/target/dependency/*:${JAR_FILE}" com.example.kuromoji_api_docker.App
