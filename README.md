# Spear
Reference API project using [Javalin](https://javalin.io/), [NGINX](https://docs.nginx.com/), [Docker](https://docs.docker.com/), [HSQLDB](https://hsqldb.org/) and [JWT](https://jwt.io/) authentication

# Development Setup
This project was developed using these tools and technologies
- Ubuntu 24.04.1 LTS
- [IntelliJ Idea](https://www.jetbrains.com/idea/) 2024.3.1.1
- JDK 21.0.5 (Azul Zulu)
- [Maven](https://maven.apache.org/) 3.9.9
- Docker 27.1.1

# Build Command
Build an executable Jar with manifest and create a directory containing all dependencies

```mvn clean package```

# Docker Commands
Build the image

```docker compose build```

Run the application

```docker compose up --detach```

Stop the application

```docker compose down```

# SSL Commands
Generate a self-signed certificate for development purposes

```openssl req -newkey rsa:2048 -nodes -keyout spear.key -out spear.csr```

```openssl x509 -signkey spear.key -in spear.csr -req -days 365 -out spear.crt```
