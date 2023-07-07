FROM maven:3-jdk-11-slim as build

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn clean package

FROM adoptopenjdk/openjdk11:jdk-11.0.19_7-slim as production

ENV APP_USER_HOME /home/appuser

RUN groupadd -g 999 appuser && \
  useradd -r -u 999 -g appuser -d $APP_USER_HOME appuser && \
  mkdir -p -m 700 $APP_USER_HOME &&\
  chown -R appuser $APP_USER_HOME

USER appuser

COPY --from=build --chown=appuser /usr/src/app/target/diga-api-service-*.jar diga-api-service.jar
COPY --chown=appuser ./diga_api_files diga_api_files

EXPOSE 5000

CMD ["java", "-jar", "/diga-api-service.jar"]
