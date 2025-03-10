# Use Eclipse Temurin JDK 21 Alpine as a parent image
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Set the working directory inside the container
WORKDIR /app

# Switch to the non-root user
USER spring

# Copy the built JAR file into the container
COPY target/Livrili-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]