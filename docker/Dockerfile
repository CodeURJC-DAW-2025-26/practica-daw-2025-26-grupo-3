# Base image for the build container
FROM maven:3.9-eclipse-temurin-21 AS builder

# Define the working directory to execute commands
WORKDIR /project

# Copy project dependencies
COPY backend/pom.xml .

# Download project dependencies
RUN mvn dependency:go-offline

# Copy the project code
COPY backend/src ./src

# Compile project
RUN mvn -B package -DskipTests

# Base image for the application container
FROM eclipse-temurin:21-jre

# Define the working directory where the JAR is located
WORKDIR /app

# Copy the JAR from the build container
COPY --from=builder /project/target/*.jar ./app.jar

# Indicate the port that the container exposes
EXPOSE 8443 

# Command to be executed when running docker run
ENTRYPOINT [ "java", "-jar", "app.jar" ]