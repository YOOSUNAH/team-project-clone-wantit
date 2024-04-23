FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} docker-wantit.jar

ENV port 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-wantit.jar"]
