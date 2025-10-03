package com.fiap.hospital.bff.core.domain.model.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String accessToken;
    private Long expiresIn;
    private List<String> scopes;
}