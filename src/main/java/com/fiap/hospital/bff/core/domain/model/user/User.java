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
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDate changeDate;
    private Type type;

    public User(
            String name,
            String email,
            String password,
            Type type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }
}

