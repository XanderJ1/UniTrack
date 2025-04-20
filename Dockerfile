FROM openjdk:21-jdk

WORKDIR /app

COPY target/Unitrack-0.0.1-SNAPSHOT.jar /app/Unitrack-0.0.1-SNAPSHOT.jar

EXPOSE 8081

CMD ["java", "-jar", "Unitrack-0.0.1-SNAPSHOT.jar"]