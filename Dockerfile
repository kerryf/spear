FROM docker.io/azul/zulu-openjdk:21-latest

# Create a working directory for our application
WORKDIR /opt/spear

# Create project directories
RUN mkdir "conf"
RUN mkdir "logs"
RUN mkdir "data"

# Copy project files to the working directory
COPY scripts scripts
COPY build/dependency-jars dependency-jars
COPY build/libs/spear.jar spear.jar

# Have the container listen on port 8081
EXPOSE 8081

# Start the application
ENTRYPOINT ["java", "-Djava.util.logging.config.file=conf/Logging.properties", "-jar", "spear.jar"]
