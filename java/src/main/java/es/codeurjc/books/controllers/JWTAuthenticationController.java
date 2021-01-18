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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1")
public class JWTAuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Authenticates user with nick and password and returns a token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Nick and password of the user",
        required = true,
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthRequestDto.class)
    ))
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200", 
                description = "Credentials ok and token obtained",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserAuthResponse.class))
                }
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Unauthorized due to wrong credentials",
                content = @Content
            )
        }
    )
    @PostMapping("/auth")
    public ResponseEntity<Object> authenticateUser(@RequestBody UserAuthRequestDto user) {

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
