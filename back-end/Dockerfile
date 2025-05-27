FROM amazoncorretto:17-alpine-jdk

# Create directory for application
WORKDIR /app

# Copy jar file into container
COPY target/wipolex-rag.jar /app/

# Expose application port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "wipolex-rag.jar"]