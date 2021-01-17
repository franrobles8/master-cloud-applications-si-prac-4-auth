package es.codeurjc.books.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import es.codeurjc.books.dtos.requests.UserAuthRequestDto;
import es.codeurjc.books.dtos.responses.UserAuthResponse;
import es.codeurjc.books.security.JwtUtils;

@RestController
@RequestMapping("/api/v1")
public class JWTAuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/auth")
    public ResponseEntity<Object> authenticateUser(@RequestBody UserAuthRequestDto user) throws Exception {

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getNick(), user.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect nick or password", HttpStatus.UNAUTHORIZED);
        }
        
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getNick());

        String token = jwtUtils.generateToken(userDetails);
        
        return ResponseEntity.ok(new UserAuthResponse(token));
    }
}
