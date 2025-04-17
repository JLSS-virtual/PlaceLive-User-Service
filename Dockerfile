# Use official OpenJDK base image
FROM openjdk:17

# Metadata (optional)
LABEL maintainer="solan"

# Build-time argument to copy the JAR
ARG JAR_FILE=target/*.jar

# Copy the JAR to the container
COPY ${JAR_FILE} app.jar

# Define the entrypoint
ENTRYPOINT ["java", "-jar", "/app.jar"]
