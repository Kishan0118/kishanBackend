# Base image
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose the port
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "target/kishanBackend-0.0.1-SNAPSHOT.jar"]