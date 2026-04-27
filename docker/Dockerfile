# --- Phase 1: Frontend Build (React SPA) ---
FROM node:20 AS frontend-builder

# Define the working directory for the frontend
WORKDIR /frontend

# Copy dependency files and install (leveraging Docker cache)
COPY frontend/package*.json ./
RUN npm install

# Copy the rest of the frontend code and build
COPY frontend/ ./
RUN npm run build


# --- Phase 2: Backend Build (Spring Boot) ---
FROM maven:3.9-eclipse-temurin-21 AS backend-builder

# Define the working directory for the backend
WORKDIR /project

# Copy Maven dependencies and download them
COPY backend/pom.xml .
RUN mvn dependency:go-offline

# Copy the Spring Boot source code
COPY backend/src ./src

# THE MAGIC HAPPENS HERE!
# Copy the compiled SPA from Phase 1 to the Spring Boot static resources folder, 
# specifically into the "new" subfolder so it responds at /new/
# IMPORTANT: Change "dist" to "build" if you used Create React App instead of Vite.
COPY --from=frontend-builder /frontend/build/client ./src/main/resources/static/new

# Compile the project (the resulting JAR will include both the traditional web and the SPA)
RUN mvn -B package -DskipTests

# --- Phase 3: Final Image (Runtime environment only) ---
FROM eclipse-temurin:21-jre

# Define the working directory where the JAR will be located
WORKDIR /app

# Copy the compiled JAR from Phase 2
COPY --from=backend-builder /project/target/*.jar ./app.jar

# Indicate the port that the container exposes
EXPOSE 8443 

# Command to be executed when starting
ENTRYPOINT [ "java", "-jar", "app.jar" ]