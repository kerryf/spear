# Spear
Reference API project using [Javalin](https://javalin.io/), [NGINX](https://docs.nginx.com/), [Docker](https://docs.docker.com/), [HSQLDB](https://hsqldb.org/), [Groovy](https://groovy-lang.org/) and [JWT](https://jwt.io/) authentication

# Development Setup
This project was developed using these tools and technologies
- Ubuntu 24.04.1 LTS
- [IntelliJ Idea](https://www.jetbrains.com/idea/) 2024.3.1.1
- JDK 21.0.5 (Azul Zulu)
- [Gradle](https://gradle.org/) 8.7 (or use the included Gradle Wrapper)
- Docker 27.1.1

# Build Command
Build an executable Jar with manifest and create a directory containing all dependencies

Using Gradle:
```
./gradlew clean build
```

Or using Maven (legacy):
```
mvn clean package
```

# Docker Commands
Build the image

```docker compose build```

Run the application

```docker compose up --detach```

Stop the application

```docker compose down```

# Groovy Support
This project includes support for [Groovy](https://groovy-lang.org/) programming language. Groovy is a powerful, optionally typed and dynamic language for the Java platform.

## Groovy Source Directories
- Main Groovy code: `src/main/groovy`
- Test Groovy code: `src/test/groovy`

## Writing Groovy Code
You can write Groovy classes alongside Java classes. For example:

```groovy
package spear.utils

class StringUtils {
    static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty()
    }

    // More methods...
}
```

## Testing Groovy Code
The project uses [Spock Framework](https://spockframework.org/) for testing Groovy code. Spock is a testing and specification framework that makes it easy to write expressive and readable tests.

Example test:
```groovy
package spear.utils

import spock.lang.Specification

class StringUtilsSpec extends Specification {
    def "isEmpty should return true for null or empty strings"() {
        expect:
        StringUtils.isEmpty(input) == expected

        where:
        input    | expected
        null     | true
        ""       | true
        "test"   | false
    }
}
```

# SSL Commands
Generate a self-signed certificate for development purposes

```openssl req -newkey rsa:2048 -nodes -keyout spear.key -out spear.csr```

```openssl x509 -signkey spear.key -in spear.csr -req -days 365 -out spear.crt```
