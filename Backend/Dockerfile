# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Add a volume pointing to /tmp
VOLUME /tmp

# Copy the JAR file from your target directory to the image
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]