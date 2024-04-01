# syntax=docker/dockerfile:1
FROM mcr.microsoft.com/mssql/server:2019-latest as app

LABEL name="nmea-sql"

USER root

ENV ACCEPT_EULA=Y
ENV MSSQL_SA_PASSWORD=P@ssw0rd
ENV DISPLAY=host.docker.internal:0.0

RUN apt-get update && apt-get install -y \
    openjdk-8-jre \
    curl \
    gnupg2 \
    openjfx \
    git

WORKDIR /app/
RUN git clone https://github.com/vyachhhh/NMEA-Converter-for-MR-231-3.git
WORKDIR /NMEA-Converter-for-MR-231-3-master
ENTRYPOINT ["java", "-Dprism.order=sw", "-jar", "javafxApp.jar"]
#CMD ["/mssql.sh"]
#ENTRYPOINT ["java","-Dprism.order=sw","-jar","javafxApp-1.0-SNAPSHOT-jar-with-dependencies.jar"]
#FROM maven:3.9.6-amazoncorretto-8-debian as app
#    libx11-dev \
#    libx11-6 \
#    libgl1-mesa-glx \
#    libgtk-3-0 \