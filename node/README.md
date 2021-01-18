# Practice 4. Authentication and Authorization with Node, MongoDB and JWT.

## Usage

```sh
$ docker run --name mongo-db  -p 27017:27017 -d mongo:latest
$ npm install
$ npm start
```
## API Root URL

```sh
http://localhost:3443/api/v1/<resource>
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

- This version of the API uses https, you should add a `server.cert` and `server.key` at the same level than the `package.json` file. They can be created with the following command:

```sh
openssl req -nodes -new -x509 -keyout server.key -out server.cert
```

## Postman Collection attached

Contains a folder with a Postman Collection in order to use the API.

## Authentication/Authorization made by:

👤 Francisco Robles Castro

* Github: [@franrobles8](https://github.com/franrobles8)
