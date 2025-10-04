package com.fiap.hospital.bff.infra.persistence.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String login;

    private String password;

    @Column(name = "change_date")
    private LocalDate changeDate;

    @ManyToOne
    @JoinColumn(name = "types", referencedColumnName = "id")
    private TypeEntity types;

    public UserEntity() {
        // Default constructor required by JPA
    }
}
