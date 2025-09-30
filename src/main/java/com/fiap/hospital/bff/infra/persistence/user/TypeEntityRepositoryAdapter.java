package com.fiap.hospital.bff.infra.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeEntityRepositoryAdapter extends JpaRepository<TypeEntity, Long> {
    Optional<TypeEntity> findByNameType(String name);
    Optional<TypeEntity> findById(String name);
}
