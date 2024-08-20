FROM amazoncorretto:21-alpine3.18-jdk

COPY rest/target/rest-0.0.1-SNAPSHOT.jar comments-app.jar

ENTRYPOINT ["java","-jar","/comments-app.jar"]
