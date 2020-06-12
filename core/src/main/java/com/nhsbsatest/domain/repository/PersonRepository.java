package com.nhsbsatest.domain.repository;

import com.nhsbsatest.domain.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByEmail(String email);

    Optional<PersonEntity> findByPlatformPersonIdentifier(String platformPersonIdentifier);
}