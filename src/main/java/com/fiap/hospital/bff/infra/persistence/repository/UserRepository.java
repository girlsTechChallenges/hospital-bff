package com.fiap.hospital.bff.infra.persistence.repository;

import com.fiap.hospital.bff.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.type WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithType(@Param("email") String email);

}
