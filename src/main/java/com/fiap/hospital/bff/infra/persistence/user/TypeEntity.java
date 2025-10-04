package com.fiap.hospital.bff.infra.persistence.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Builder
@Table(name = "types")
public class TypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameType;

    @ElementCollection
    private List<String> roles;

    public TypeEntity() {}

    public TypeEntity(Long id, String nome, List<String> roles) {
        this.id = id;
        this.nameType = nome;
        this.roles = roles;
    }
}
