FROM amazoncorretto:17.0.8-alpine3.18

# Create a working directory for our application
WORKDIR /opt/spear

# Create the logs directory
RUN mkdir "logs"

# Copy project files to the working directory
COPY Dockerfile .
COPY conf conf
COPY target/dependency-jars dependency-jars
COPY target/spear.jar spear.jar

# Have the container listen on port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-Djava.util.logging.config.file=conf/Logging.properties", "-jar", "spear.jar"]
