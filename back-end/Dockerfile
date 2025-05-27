FROM amazoncorretto:17-alpine-jdk

# Create directory for application
WORKDIR /app

# Copy jar file into container
COPY target/wipolex-rag.jar /app/wipolex-rag.jar
RUN chmod +x /app/wipolex-rag.jar
# Expose application port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "/app/wipolex-rag.jar"]
