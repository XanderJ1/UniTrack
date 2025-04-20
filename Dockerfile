FROM maven:3.8.8-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 80

CMD ["java", "-jar", "app.jar"]
