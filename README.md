# Spear
Reference API project using Javalin and Docker

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
