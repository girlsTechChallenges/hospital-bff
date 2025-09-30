package com.fiap.hospital.bff.core.domain.model.user;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Type {
    Long id;
    String nameType;
    List<String> roles;
}
