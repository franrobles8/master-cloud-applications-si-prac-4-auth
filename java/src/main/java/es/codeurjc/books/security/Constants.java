package es.codeurjc.books.security;

public class Constants {

    // Spring Security
    protected static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    protected static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    protected static final String ISSUER = "exampleIssuer";
    protected static final String JWT_SECRET = "MySuperSecret";
    protected static final long TOKEN_EXPIRATION_TIME = 864_000_000;
}
