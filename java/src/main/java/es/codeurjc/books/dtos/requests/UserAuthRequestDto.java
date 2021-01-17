package es.codeurjc.books.dtos.requests;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserAuthRequestDto {

    @NotBlank(message = "Nick is mandatory")
    private String nick;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
