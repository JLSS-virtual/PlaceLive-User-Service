# Use official OpenJDK base image
FROM openjdk:17

# Metadata
LABEL maintainer="solan"


# Build-time argument to copy the JAR
ARG JAR_FILE=target/*.jar

# Copy the JAR to the container
COPY ${JAR_FILE} app.jar

# Define the entrypoint
# Use a shell form so $PORT is expanded at runtime
ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar /app.jar"]

