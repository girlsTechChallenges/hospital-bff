package com.fiap.hospital.bff.infra.persistence.repository;

import com.fiap.hospital.bff.infra.persistence.entity.TypeEntity;
import com.fiap.hospital.bff.infra.persistence.entity.UserTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {

    Optional<TypeEntity> findByNameType(String nameType);

    Optional<TypeEntity> findByUserType(UserTypeEnum userType);

    boolean existsByNameType(String nameType);

    boolean existsByUserType(UserTypeEnum userType);

    @Query("SELECT t FROM TypeEntity t LEFT JOIN FETCH t.roles WHERE t.nameType = :nameType")
    Optional<TypeEntity> findByNameTypeWithRoles(@Param("nameType") String nameType);

    @Query("SELECT t FROM TypeEntity t WHERE t.userType IN :userTypes")
    List<TypeEntity> findByUserTypeIn(@Param("userTypes") List<UserTypeEnum> userTypes);
}
