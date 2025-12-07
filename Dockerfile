FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy all project files
COPY . .

# Make mvnw executable
RUN chmod +x mvnw
RUN chmod +x mvnw.cmd,

# Build the project
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/kishanBackend-0.0.1-SNAPSHOT.jar"]