# ===============================
# 1. Build Stage (Maven + JDK)
# ===============================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy entire project
COPY . .

# Give permission to mvnw (important)
RUN chmod +x mvnw

# Build the project without tests
RUN ./mvnw clean package -DskipTests


# ===============================
# 2. Run Stage (Lightweight JRE)
# ===============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy only JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar","app.jar"]
