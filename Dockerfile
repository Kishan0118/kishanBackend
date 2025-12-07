# Base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Start the backend
CMD ["java", "-jar", "target/kishanBackend-0.0.1-SNAPSHOT.jar"]