FROM openjdk:8-jdk-alpine
MAINTAINER javamuscles
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} jmuscles-rabbitmq-consumer-app-j8sb2713.jar
ENTRYPOINT ["java","-jar", \
"-Dcom.sun.management.jmxremote=true", \
"-Dcom.sun.management.jmxremote.port=9010", \
"-Dcom.sun.management.jmxremote.local.only=false", \
"-Dcom.sun.management.jmxremote.authenticate=false", \
"-Dcom.sun.management.jmxremote.ssl=false", \
"-Dcom.sun.management.jmxremote.rmi.port=9010", \
"-Djava.rmi.server.hostname=localhost", \
"/jmuscles-rabbitmq-consumer-app-j8sb2713.jar"]
Expose 9010
Expose 8080
Expose 5671
Expose 9871

