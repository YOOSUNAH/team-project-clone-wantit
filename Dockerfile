FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} docker-wantit.jar

ENV port 8080
ENV client_id 80bee01a24a6fbf2f1941a7483488338
ENV database.name wantit_backend_db
ENV email.password wyamzwfghvfuiqjr doc
ENV email.username dcnwantit@gmail.com
ENV grant_type authorization_code
ENV redirect_uri http://localhost:8080/v1/users/kakao/callback
ENV mysql.username wantitadmin
ENV mysql.password wantit0419
ENV jwt.secret.key 642V7LKg64Sk7Yq47JuN7Iqk7J2YIOychOuMgO2VnCDssqsg6rG47J2MIQ==

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-wantit.jar"]
