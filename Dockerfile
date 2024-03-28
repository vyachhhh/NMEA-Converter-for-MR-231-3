# syntax=docker/dockerfile:1
FROM maven:3.9.6-amazoncorretto-8-debian as app

ENV DISPLAY=host.docker.internal:0.0

COPY / /app/

WORKDIR /app/build
RUN mvn clean package

RUN apt-get update && apt-get install -y \
    curl \
    gnupg2 \
    openjfx

WORKDIR ../javafxApp/target
ENTRYPOINT ["java","-Dprism.order=sw","-jar","javafxApp-1.0-SNAPSHOT-jar-with-dependencies.jar"]

#    libx11-dev \
#    libx11-6 \
#    libgl1-mesa-glx \
#    libgtk-3-0 \