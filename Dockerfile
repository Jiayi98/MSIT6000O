FROM amazoncorretto:8
VOLUME /tmp
ARG JAR_FILE
ADD /target/msit6000o-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]

