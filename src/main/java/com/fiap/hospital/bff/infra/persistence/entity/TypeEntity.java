package com.fiap.hospital.bff.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_type", nullable = false, unique = true)
    private String nameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserTypeEnum userType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "type_roles", joinColumns = @JoinColumn(name = "type_id"))
    @Column(name = "role")
    private Set<String> roles;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> users = new ArrayList<>();
}