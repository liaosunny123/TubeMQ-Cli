#!/bin/bash

JAR_NAME="tubemq-cli-${project.version}-jar-with-dependencies.jar"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JAR="$DIR/$JAR_NAME"

java -jar $JAR "$@"