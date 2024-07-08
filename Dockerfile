FROM azul/zulu-openjdk:21.0.3

# Create a working directory for our application
WORKDIR /opt/spear

# Create project directories
RUN mkdir "conf"
RUN mkdir "logs"
RUN mkdir "data"

# Copy project files to the working directory
COPY scripts scripts
COPY target/dependency-jars dependency-jars
COPY target/spear.jar spear.jar

# Have the container listen on port 8080
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-Djava.util.logging.config.file=conf/Logging.properties", "-jar", "spear.jar"]
