# -------- Build Stage --------
FROM maven:3.8.8-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy only pom.xml first (to leverage Docker cache)
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Now copy the rest of the project
COPY src ./src

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Application port (change if your app uses a different one)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
