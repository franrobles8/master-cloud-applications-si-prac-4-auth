# Practice 4. Authentication and Authorization with Java, SpringBoot and MySQL

## Usage

```sh
docker run -p 3306:3306 --name mysql-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest
mvn spring-boot:run
```

## API Root URL

```sh
http://localhost:8443/api/v1/<resource>
```

To use some of the routes, you may need to be authenticated in order to have the authorization. For this, you can create a new user and get a token with this two requests:

**Creates a new user:**

```sh
[POST]/api/v1/users/
```

**Authenticate the user and returns the necessary token for authorization:**

```sh
[POST]/api/v1/auth
```

**Advices:**

- To use the token, you can paste it in a Rest Client like Postman, setting it as a **Bearer token** with the right value (To make it easier, you could set this token for all the collection and with that, you should have it for every request).

- This version of the API uses https, you should add a `keystore.jks` under the `/resources` directory. It can be created with the following command:

```sh
cd C:\Program Files\Java\jdk-11.0.5\bin

keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 360 -keysize 2048
```

## API Docs URL

```sh
https://localhost:8443/api-docs
https://localhost:8443/api-docs.yaml
```

## API Swagger URL

```sh
http://localhost:8443/swagger-ui.html
```

## Initial code by:

👤 Álvaro Martín Martín

* Github: [@amartinm82](https://github.com/amartinm82)

## Authentication/Authorization made by:

👤 Francisco Robles Castro

* Github: [@franrobles8](https://github.com/franrobles8)

Note that you should have to [install JDK 14](https://www.oracle.com/es/java/technologies/javase/jdk14-archive-downloads.html), [Maven](https://maven.apache.org/install.html) and [Docker](https://docs.docker.com/engine/install/) as prerequisite.
