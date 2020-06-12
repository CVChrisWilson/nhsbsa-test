package com.nhsbsatest.domain;

import com.nhsbsatest.domain.entity.PersonEntity;
import com.nhsbsatest.domain.repository.PersonRepository;
import com.nhsbsatest.domain.rest.exception.ConflictException;
import com.nhsbsatest.domain.rest.exception.NotFoundException;
import com.nhsbsatest.model.PlatformIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class PersonManager {

    @Autowired
    private PersonRepository personRepository;

    ExecutorService executorService = Executors.newCachedThreadPool();

    public CompletableFuture<PersonEntity> createPerson(com.nhsbsatest.model.NewPerson newPerson) {
        return findPerson(newPerson.getEmail())
                .thenCompose(personEntity -> {
                    if (personEntity == null) {
                        return upsertPerson(new PersonEntity(newPerson));
                    } else {
                        throw new CompletionException(new ConflictException());
                    }
                });
    }

    public CompletableFuture<PersonEntity> updatePerson(com.nhsbsatest.model.Person person) {
        return getPerson(person.getPlatformPersonIdentifier())
                .thenCompose(foundPersonEntity -> {
                    if (foundPersonEntity != null) {
                        PersonEntity personEntity = new PersonEntity(person);
                        personEntity.setId(foundPersonEntity.getId());
                        return upsertPerson(personEntity);
                    } else {
                        throw new CompletionException(new NotFoundException());
                    }
                });
    }

    public CompletableFuture<PersonEntity> upsertPerson(PersonEntity personEntity) {
        return CompletableFuture.supplyAsync(() -> {
            personEntity.getSkills()
                    .forEach(skill -> skill.setPersonEntity(personEntity));
            return personRepository.save(personEntity);
        });
    }

    public CompletableFuture<Void> deletePerson(PlatformIdentifier platformPersonIdentifier) {
        return getPerson(platformPersonIdentifier)
                .thenAccept(personEntity -> {
                    if (personEntity != null) {
                        personRepository.deleteById(personEntity.getId());
                    }
                });
    }

    public CompletableFuture<PersonEntity> getPerson(PlatformIdentifier platformPersonIdentifier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return personRepository.findByPlatformPersonIdentifier(platformPersonIdentifier.toString()).orElseThrow(NotFoundException::new);
            } catch (NotFoundException e) {
                throw new CompletionException(e);
            }
        });
    }

    private CompletableFuture<PersonEntity> findPerson(String email) {
        return CompletableFuture.supplyAsync(() -> personRepository.findByEmail(email).orElse(null));
    }
}
