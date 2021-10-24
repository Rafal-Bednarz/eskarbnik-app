FROM maven:3.6.0-jdk-11-slim AS build
COPY e-skarbnik eskarbnik-spa/e-skarbnik
COPY e-skarbnik-api eskarbnik-spa/e-skarbnik-api
COPY e-skarbnik-data eskarbnik-spa/e-skarbnik-data
COPY e-skarbnik-domain eskarbnik-spa/e-skarbnik-domain
COPY e-skarbnik-security eskarbnik-spa/e-skarbnik-security
COPY e-skarbnik-web eskarbnik-spa/e-skarbnik-web
COPY lib eskarbnik-spa/lib
COPY Dockerfile eskarbnik-spa/
COPY system.properties eskarbnik-spa/
COPY .gitignore eskarbnik-spa/
COPY pom.xml eskarbnik-spa/
RUN mvn -f eskarbnik-spa/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build eskarbnik-spa/e-skarbnik/target/e-skarbnik-3.0.0-SNAPSHOT.jar /usr/local/lib/e-skarbnik.jar

ENTRYPOINT ["java","-Xms256m", "-Xmx256m", "-Xss512k","-jar","/usr/local/lib/e-skarbnik.jar"]