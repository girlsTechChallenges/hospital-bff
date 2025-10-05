package com.fiap.hospital.bff.infra.persistence.entity;

import com.fiap.hospital.bff.infra.entrypoint.dto.request.TypeUsers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String login;

    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TypeUsers tipo;

}