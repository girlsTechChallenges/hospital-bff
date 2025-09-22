package com.fiap.hospital.bff.infra.persistence.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
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
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TypeEntity type;

    public UserEntity() {}

    private UserEntity(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.login = builder.login;
        this.password = builder.password;
        this.changeDate = builder.changeDate;
        this.type = builder.type;

    }

    @PrePersist
    @PreUpdate
    public void setChangeDate() {
        this.changeDate = LocalDate.now();
    }

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String senha) {
        this.password = senha;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String login;
        private String password;
        private LocalDate changeDate;
        private TypeEntity type;


        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String nome) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder changeDate(LocalDate changeDate) {
            this.changeDate = changeDate;
            return this;
        }

        public Builder type(TypeEntity type) {
            this.type = type;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
