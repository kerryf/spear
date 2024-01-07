# Spear
Reference API project using [Javalin](https://javalin.io/) and [Docker](https://docs.docker.com/)

# Development Setup
This project was developed using these tools and technologies
- Fedora 39
- [IntelliJ Idea](https://www.jetbrains.com/idea/) 2023.3.2
- JDK 21.0.1 (Azul Zulu)
- [Maven](https://maven.apache.org/) 3.9.5
- Docker 24.0.7

# Build Command
Build an executable Jar with manifest and create a directory containing all dependencies

```mvn clean package```

# Docker Commands
Build and tag the resulting image

```docker build --tag spear-api:v1 --no-cache .```

Run the image

```docker run --name spear --env-file .env --publish 8080:8080 --detach spear-api:v1```

| Option                | Description                                             |
|-----------------------|---------------------------------------------------------|
| `--name spear`        | Name the container 'spear'                              |
| `--env-file .env`     | Load environment variables from the '.env' file         |
| `--publish 8080:8080` | Map port 8080 on the host to port 8080 on the container |
| `--detach`            | Run the container in the background                     |

Stop the container

```docker stop spear```
