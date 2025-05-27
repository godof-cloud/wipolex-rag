FROM registry.wipo.int/docker.io/library/amazoncorretto:17-alpine-jdk

# Install artifact
VOLUME /app
COPY target/wipolex-rag.jar /app/wipolex-rag.jar

EXPOSE 8080

WORKDIR /app
ENTRYPOINT ["java","-jar", "wipolex-rag.jar"]
