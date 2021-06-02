FROM maven:3-jdk-11-slim

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn package -DskipTests

CMD [ "sh", "-c", "mvn spring-boot:run" ]
