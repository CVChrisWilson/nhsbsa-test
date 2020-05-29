package com.nhsbsatest.domain;

import com.nhsbsatest.domain.entity.Person;
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

    public CompletableFuture<Person> createPerson(com.nhsbsatest.model.NewPerson newPerson) {
        return findPerson(newPerson.getEmail())
                .thenCompose(person -> {
                    if (person == null) {
                        return upsertPerson(new Person(newPerson));
                    } else {
                        throw new CompletionException(new ConflictException());
                    }
                });
    }

    public CompletableFuture<Person> updatePerson(com.nhsbsatest.model.Person person) {
        return getPerson(person.getPlatformPersonIdentifier())
                .thenCompose(foundPerson -> {
                    if (foundPerson != null) {
                        Person personEntity = new Person(person);
                        personEntity.setId(foundPerson.getId());
                        return upsertPerson(personEntity);
                    } else {
                        throw new CompletionException(new NotFoundException());
                    }
                });
    }

    public CompletableFuture<Person> upsertPerson(com.nhsbsatest.domain.entity.Person person) {
        return CompletableFuture.supplyAsync(() -> {
            person.getSkills()
                    .forEach(skill -> skill.setPerson(person));
            return personRepository.save(person);
        });
    }

    public CompletableFuture<Void> deletePerson(PlatformIdentifier platformPersonIdentifier) {
        return getPerson(platformPersonIdentifier)
                .thenAccept(person -> {
                    if (person != null) {
                        personRepository.deleteById(person.getId());
                    }
                });
    }

    public CompletableFuture<Person> getPerson(PlatformIdentifier platformPersonIdentifier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return personRepository.findByPlatformPersonIdentifier(platformPersonIdentifier.toString()).orElseThrow(NotFoundException::new);
            } catch (NotFoundException e) {
                throw new CompletionException(e);
            }
        });
    }

    private CompletableFuture<Person> findPerson(String email) {
        return CompletableFuture.supplyAsync(() -> personRepository.findByEmail(email).orElse(null));
    }
}
