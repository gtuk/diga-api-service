FROM maven:3-jdk-11-slim as build

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn clean package

FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim as production

COPY --from=build /usr/src/app/target/diga-api-service-*.jar /diga-api-service.jar

CMD ["java", "-jar", "/diga-api-service.jar"]

