package com.fiap.hospital.bff.core.domain.model.user;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String nome;
    private String email;
    private String login;
    private String senha;
    private LocalDate dataAlteracao;
    private String tipo;

    public User(
            String nome,
            String email,
            String senha,
            String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }
}
