package com.nhsbsatest.domain.repository;

import com.nhsbsatest.domain.entity.Person;
import com.nhsbsatest.model.PlatformIdentifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByEmail(String email);

    Optional<Person> findByPlatformPersonIdentifier(String platformPersonIdentifier);
}