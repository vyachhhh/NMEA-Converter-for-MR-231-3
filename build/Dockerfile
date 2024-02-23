# syntax=docker/dockerfile:1



################################################################################


FROM eclipse-temurin:8-jre-jammy AS final

# Create a non-privileged user that the app will run under.
# See https://docs.docker.com/go/dockerfile-user-best-practices/

RUN mkdir /app

##COPY ../mr231/target/mr-231-1.0-SNAPSHOT.jar /app/mr-231-1.0-SNAPSHOT.jar
##COPY ../mr2313/target/mr-231-3-1.0-SNAPSHOT.jar /app/mr-231-3-1.0-SNAPSHOT.jar
##COPY ../startApp/target/startApp-1.0-SNAPSHOT.jar /app/startApp-1.0-SNAPSHOT.jar
##COPY ../testModule/target/testModule-1.0-SNAPSHOT.jar /app/testModule-1.0-SNAPSHOT.jar

#COPY mr-231-1.0-SNAPSHOT.jar /app/mr-231-1.0-SNAPSHOT.jar
#COPY mr-231-3-1.0-SNAPSHOT.jar /app/mr-231-3-1.0-SNAPSHOT.jar
COPY startApp-1.0-SNAPSHOT-jar-with-dependencies.jar /app/startApp-1.0-SNAPSHOT.jar
#COPY testModule-1.0-SNAPSHOT.jar /app/testModule-1.0-SNAPSHOT.jar

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

EXPOSE 8088

##ENTRYPOINT [ "java", "-jar", "app.jar" ]

WORKDIR /app
CMD ["java", "-jar", "startApp-1.0-SNAPSHOT.jar"]
