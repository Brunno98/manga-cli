#!/usr/bin bash

# This script aims to easy runnig the shell application on IDE because the default runner doesn't work
# with spring shell

./mvnw clean install -DskipTests
if [ $? -eq 0 ]; then
  clear
fi

java -jar target/manga-cli-0.0.1-SNAPSHOT.jar