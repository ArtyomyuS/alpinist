#!/usr/bin/env bash
mvn clean package install assembly:single &&
java -javaagent:/Users/artiom.darie/_/_projects/open-source/alpinist/target/alpinist-1.0.0-SNAPSHOT.jar \
    -cp /Users/artiom.darie/_/_projects/open-source/alpinist/target/alpinist-1.0.0-SNAPSHOT.jar \
    com.adswizz.profiler.RunExample