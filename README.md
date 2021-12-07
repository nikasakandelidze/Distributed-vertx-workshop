# Vertx distributed application workshop
This repo contains vertx distributed application workshop's content.

# Build and run
To build the application use:
``` ./gradlew clean shadowJar ```
which will package application with all of it's dependencies into an executable Jar

To run the several instances of application, use several times in different shell sessions: 
``` java -Dport=<PORT_NUMBER> -jar libs/build/vertx-dist-1.0-SNAPSHOT-all.jar -cluster ```
Where you can choose PORT_NUMBER yourself.

# Implementation details
This implementation uses hazelcast's distributed map to demonstrate mechanisms of vertx cluster.
