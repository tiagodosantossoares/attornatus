FROM openjdk:11 as build
WORKDIR /usr/src/app

COPY ./test /usr/src/app/test
ENTRYPOINT ["mvn","package"]

FROM openjdk:11
ARG JAR_FILE=/usr/src/app/test/target/*.jar
WORKDIR /usr/src/app
COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]