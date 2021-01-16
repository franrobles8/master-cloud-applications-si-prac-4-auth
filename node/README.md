# Práctica 2: Node y MongoDB

## Ejecución

```sh
$ docker run --name mongo-db  -p 27017:27017 -d mongo:latest
$ npm install
$ npm start
```
## API URL

```sh
https://localhost:3443/api/v1/<resource>
```

## Uso de la API

Se proporciona una colección Postman para interactuar con la API.

La baseURL tiene que configurarse para https://localhost:3443

La base de datos se inicializa con datos de ejemplo

## Información token de acceso

El token de acceso para poder acceder a las rutas privadas se puede conseguir llamando al endpoint **[POST]/users** (registrando un nuevo usuario), o si ya existe el usuario, llamando a **[POST]/users/token** junto con el nick y la password.

Para acceder a las rutas privadas hay que añadir el token en el header **x-access-token** al hacer las peticiones REST.