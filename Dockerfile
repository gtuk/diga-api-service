# build stage to build the diga-api-service.jar
FROM maven:3-jdk-11-slim as build

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn clean package

# create production image. Use the Temurin image with Alpine Linux and Java 17
FROM eclipse-temurin:17-alpine as production

RUN mkdir /app

# add a new user javauser so we dont run the application as root
RUN addgroup -S javauser && adduser -S -G javauser javauser

# Copy the JAR file from the build stage into the production image
COPY --from=build /usr/src/app/target/diga-api-service-*.jar /app/diga-api-service.jar

WORKDIR /app

# Set the ownership to the javauser
RUN chown -R javauser:javauser /app

# Switch to the javauser
USER javauser

# Specify the command to run your application
CMD "java" "-jar" "diga-api-service.jar"
