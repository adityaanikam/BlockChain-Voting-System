# Multi-stage Dockerfile for Blockchain Voting System
# Stage 1: Build stage
FROM openjdk:17-jdk-slim as builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy Maven files first for dependency caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (backend-only profile for Render)
RUN mvn clean package -P backend-only -DskipTests

# Stage 2: Runtime stage for backend-only deployment
FROM openjdk:17-jre-slim as backend

# Install necessary packages for headless operation
RUN apt-get update && \
    apt-get install -y \
    xvfb \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    libasound2 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables for headless operation
ENV JAVA_OPTS="-Djava.awt.headless=true -Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=production

# Create app user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to appuser
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/api/voting/health || exit 1

# Start the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Stage 3: Runtime stage for JavaFX client (optional)
FROM openjdk:17-jre-slim as javafx-client

# Install JavaFX runtime dependencies
RUN apt-get update && \
    apt-get install -y \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libxss1 \
    libgconf-2-4 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the built JAR
COPY --from=builder /app/target/*.jar app.jar

# Expose JavaFX port (if needed)
EXPOSE 8080

# Start JavaFX application
CMD ["java", "-jar", "app.jar", "--spring.main.web-application-type=none"] 