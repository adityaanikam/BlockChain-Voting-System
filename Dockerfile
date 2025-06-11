# Multi-stage Dockerfile for Blockchain Voting System
# -------- Stage 0 : Build (Maven + JDK) -----------------------------
# Using the official Maven image that already contains Temurin 17 JDK
FROM maven:3.9.7-eclipse-temurin-17 AS builder

# Enable faster dependency resolution caching
WORKDIR /workspace

# Copy pom and Maven wrapper first (leverages Docker cache)
COPY ["pom.xml", "mvnw", "./"]
COPY .mvn .mvn

# Pull down dependencies (no sources yet)
RUN ./mvnw -B dependency:go-offline

# Copy the rest of the source code
COPY src src

# Build the fat-jar (default profile = backend-only; override with --build-arg)
ARG MAVEN_PROFILE=backend-only
RUN ./mvnw -B clean package -P ${MAVEN_PROFILE} -DskipTests

# -------- Stage 1 : Runtime (Spring Boot + optional JavaFX) ---------
# A minimal Temurin JRE image *that actually exists* on Docker Hub
FROM eclipse-temurin:17-jre-slim AS runtime

LABEL org.opencontainers.image.source="https://github.com/your/repo" \
      description="Blockchain Voting System – Spring Boot backend + optional JavaFX client"

# ---- JavaFX & headless requirements ----
#   • openjfx – runtime modules
#   • xvfb   – virtual framebuffer so JavaFX can initialise without a display
#   • common native libs for GTK / Mesa / fonts
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        openjfx \
        xvfb \
        libgtk-3-0 \
        libgl1-mesa-glx \
        libasound2 \
        fontconfig && \
    rm -rf /var/lib/apt/lists/*

# Non-root user for security
RUN useradd -ms /bin/bash appuser
USER appuser

WORKDIR /app

# Copy the fat-jar from the build stage
COPY --from=builder /workspace/target/*.jar app.jar

# Environment
ENV PORT=8080 \
    SPRING_PROFILES_ACTIVE=production \
    JAVA_OPTS="-Djava.awt.headless=true -Xmx512m -Xms256m"

EXPOSE 8080

# Healthcheck for Render
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:${PORT}/api/voting/health || exit 1

# ---- Final command ----
# If JavaFX classes are present they will initialise under Xvfb; otherwise it runs purely headless.
ENTRYPOINT ["sh","-c","xvfb-run -s '-screen 0 1024x768x24' java $JAVA_OPTS -jar app.jar"]

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
COPY --from=builder /workspace/target/*.jar app.jar

# Expose JavaFX port (if needed)
EXPOSE 8080

# Start JavaFX application
CMD ["java", "-jar", "app.jar", "--spring.main.web-application-type=none"] 