package com.fiap.hospital.bff.core.domain.model.user;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private TypeUsers tipo;

    public User(
            String nome,
            String email,
            String senha,
            TypeUsers tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }
}
