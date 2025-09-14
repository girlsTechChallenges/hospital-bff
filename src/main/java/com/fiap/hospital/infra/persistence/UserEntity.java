package com.fiap.hospital.infra.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String login;

    private String senha;

    @Column(name = "data_alteracao")
    private LocalDate dataAlteracao;

    @Enumerated(EnumType.STRING)
    private TypeEntityEnum tipo;


    public UserEntity() {}

    public UserEntity(Long id, String nome, String email, String login, String senha, LocalDate dataAlteracao, TypeEntityEnum tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.dataAlteracao = dataAlteracao;
        this.tipo = tipo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    @PrePersist
    @PreUpdate
    public void setDataAlteracao() {
        this.dataAlteracao = LocalDate.now();
    }

    public TypeEntityEnum getTipo() {
        return tipo;
    }

    public void setTipo(TypeEntityEnum tipo) {
        this.tipo = tipo;
    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}