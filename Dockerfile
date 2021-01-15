# https://hub.docker.com/_/openjdk
FROM openjdk:8-jdk-alpine

RUN echo http://mirrors.aliyun.com/alpine/latest-stable/main > /etc/apk/repositories \
 && echo http://mirrors.aliyun.com/alpine/latest-stable/community >> /etc/apk/repositories \
 && apk add tzdata openssl

COPY target/bus-tracking-0.0.1-SNAPSHOT.jar /app/app.jar
RUN mkdir /workdir

EXPOSE 8080 8081
WORKDIR /workdir

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "/app/app.jar"]
