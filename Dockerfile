# Build stage
FROM maven:3.8-openjdk-8 AS build
WORKDIR /app

# Copy all projects needed for build
# (Note: Assumes context is the root of the project)
COPY jmuscles-spring ./jmuscles-spring
COPY jmuscles-rest-producer-app ./jmuscles-rest-producer-app

# Install libraries and build the app
RUN mvn -f jmuscles-spring/pom.xml clean install -DskipTests
RUN mvn -f jmuscles-rest-producer-app/pom.xml clean package -DskipTests

# Run stage
FROM openjdk:8-jdk-alpine
MAINTAINER javamuscles

WORKDIR /app
COPY --from=build /app/jmuscles-rest-producer-app/target/*.jar jmuscles-rest-producer-app.jar
COPY jmuscles-rest-producer-app/aws-opentelemetry-agent.jar opentelemetry-javaagent.jar

ENTRYPOINT [ "sh", "-c", "java -javaagent:opentelemetry-javaagent.jar $JAVA_OPTS -jar jmuscles-rest-producer-app.jar" ]