# syntax=docker/dockerfile:1
FROM openjdk:8-jre
#FROM eclipse-temurin:8-jre-jammy AS final

#RUN apt-get install openjfx
#RUN apt-get update && apt-get install -y default-jdk
#RUN apt-get update && apt-get install -y x11-apps
#ENV DISPLAY=127.0.0.1:0
RUN mkdir /app

#COPY startApp/target/startApp-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
COPY build/javafxApp.jar /app/app.jar

WORKDIR /app

ENTRYPOINT ["java","-jar","app.jar"]
#ENTRYPOINT ["java","org.example.App"]

#CMD ["java", "-Dprism.order=sw", "-jar", "javafxApp.jar"]