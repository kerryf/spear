# Spear
Reference API project using [Javalin](https://javalin.io/) and [Docker](https://docs.docker.com/)

# Development Setup
This project was developed using these tools and technologies
- Windows 11 with WSL 2
- [IntelliJ Idea](https://www.jetbrains.com/idea/) 2023.3
- JDK 17.0.8 (Amazon Corretto)
- [Maven](https://maven.apache.org/) 3.9.5
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) 4.26.0

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
