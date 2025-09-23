package com.fiap.hospital.bff.core.domain.model.user;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Type {
    String nameType;
    List<String> roles;
}
