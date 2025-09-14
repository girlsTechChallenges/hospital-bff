package com.fiap.hospital.infra.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private TypeEnum tipo;

    public User(
            String nome,
            String email,
            String senha,
            TypeEnum tipo){}

}
