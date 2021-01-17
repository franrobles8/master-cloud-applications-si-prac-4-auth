package es.codeurjc.books.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import es.codeurjc.books.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    public AuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            User credentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return this.authManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getNick(),
                    credentials.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        String token = Jwts.builder().setIssuedAt(new Date()).setIssuer(Constants.ISSUER)
                .setSubject(((User) auth.getPrincipal()).getNick())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET).compact();

        response.addHeader(Constants.HEADER_AUTHORIZATION_KEY, Constants.TOKEN_BEARER_PREFIX + token);
    }

}
