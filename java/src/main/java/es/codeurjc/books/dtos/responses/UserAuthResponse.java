package es.codeurjc.books.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthResponse {
    private String token;
}
