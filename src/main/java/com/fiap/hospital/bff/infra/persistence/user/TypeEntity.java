package com.fiap.hospital.bff.infra.persistence.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tipos")
public class TypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    private List<String> roles;

    public TypeEntity() {}

    public TypeEntity(Long id, String nome, List<String> roles) {
        this.id = id;
        this.name = nome;
        this.roles = roles;
    }


}
