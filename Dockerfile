#Build frontend
FROM node:18-alpine as frontend-build
WORKDIR /frontend

# Install dependencies and build
COPY frontend/package*.json ./
COPY frontend/package-lock.json* ./
RUN npm install --legacy-peer-deps --no-audit --no-fund || npm install --no-audit --no-fund

# Copy frontend sources and build
COPY frontend/ ./
RUN npm run build

# Build backend with Maven, embedding frontend build into static resources
FROM maven:3.9.4-eclipse-temurin-21 as backend-build
WORKDIR /workspace

# Copy pom and sources
COPY pom.xml ./
# Copy everything except node_modules (dockerignore controls extra files)
COPY . ./

# Copy built frontend into Spring Boot's static resources so it is served by the app
RUN mkdir -p src/main/resources/static
COPY --from=frontend-build /frontend/dist/ src/main/resources/static/

# Build the Spring Boot jar
RUN mvn -B -DskipTests package

# Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from the previous stage
COPY --from=backend-build /workspace/target/*.jar ./app.jar

# Expose port (Render will map externally).
EXPOSE 8080

# Start the app, allow override via $PORT (Render provides $PORT env var)
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=${PORT:-8080}"]

