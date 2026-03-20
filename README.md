# Spear
Reference API project using [Javalin](https://javalin.io/), [NGINX](https://docs.nginx.com/), [Podman](https://podman.io/), [SQLite](https://sqlite.org/), [Groovy](https://groovy-lang.org/) and [JWT](https://jwt.io/) authentication

# Development Setup
This project was developed using these tools and technologies
- Linux Mint 22.3 - Cinnamon 64-bit
- [IntelliJ Idea](https://www.jetbrains.com/idea/) 2025.3.4
- JDK 21.0.5 (Azul Zulu)
- [Gradle](https://gradle.org/) 8.7
- Podman 4.9.3

# Build Command
Build an executable Jar with manifest and create a directory containing all dependencies

Using Gradle:
```
./gradlew clean build
```

# Podman Commands
Build the image

```podman compose build```

Run the application

```podman compose up --detach```

Stop the application

```podman compose down```

# SSL Commands
Generate a self-signed certificate for development purposes

```openssl req -newkey rsa:2048 -nodes -keyout spear.key -out spear.csr```

```openssl x509 -signkey spear.key -in spear.csr -req -days 365 -out spear.crt```
