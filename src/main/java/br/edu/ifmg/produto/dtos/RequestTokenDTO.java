package br.edu.ifmg.produto.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RequestTokenDTO {
    @NotBlank(message = "Campo requerido")
    @Email(message = "Email inválido")
    private String email;

    public RequestTokenDTO () {}

    public RequestTokenDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
