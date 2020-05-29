package com.nhsbsatest.domain.rest;

import com.nhsbsatest.domain.PersonManager;
import com.nhsbsatest.domain.converters.PersonConverters;
import com.nhsbsatest.domain.rest.exception.BadRequestException;
import com.nhsbsatest.domain.rest.exception.NotFoundException;
import com.nhsbsatest.model.NewPerson;
import com.nhsbsatest.model.Person;
import com.nhsbsatest.model.PlatformIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;

@RestController()
public class PersonController {

    @Autowired
    private PersonManager personManager;

    @GetMapping("/person/{platformPersonIdentifier}")
    public DeferredResult<ResponseEntity<?>> get(@PathVariable String platformPersonIdentifier) throws NotFoundException {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            personManager.getPerson(PlatformIdentifier.valueOf(platformPersonIdentifier))
                    .whenCompleteAsync((result, throwable) -> {
                        if (throwable != null) {
                            output.setErrorResult(throwable);
                        } else {
                            output.setResult(ResponseEntity.ok(PersonConverters.toDto(result)));
                        }
                    });
        });

        return output;
    }

    @PostMapping(path = "/person", consumes = "application/json", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> create(@RequestBody NewPerson newPerson) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            if (newPerson == null) {
                output.setErrorResult(new BadRequestException());
            } else {
                personManager.createPerson(newPerson)
                        .whenCompleteAsync((result, throwable) -> {
                            if (throwable != null) {
                                output.setErrorResult(throwable);
                            } else {
                                output.setResult(ResponseEntity.ok(PersonConverters.toDto(result)));
                            }
                        });
            }
        });

        return output;
    }

    @DeleteMapping("/person/{platformPersonIdentifier}")
    public DeferredResult<ResponseEntity<?>> delete(@PathVariable String platformPersonIdentifier) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            personManager.deletePerson(PlatformIdentifier.valueOf(platformPersonIdentifier))
                    .whenCompleteAsync((result, throwable) -> {
                        if (throwable != null) {
                            output.setErrorResult(throwable);
                        } else {
                            output.setResult(ResponseEntity.noContent().build());
                        }
                    });
        });

        return output;
    }

    // differs slightly from hateoas principle (removes /person/{platformId}) to prevent platformIdentifer
    // which in a real application would act as an intercomponent Id, from being updated.
    @PutMapping(path = "/person", consumes = "application/json", produces = "application/json")
    public DeferredResult<ResponseEntity<?>> update(@RequestBody Person person) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            if (person == null) {
                output.setErrorResult(new BadRequestException());
            } else {
                personManager.updatePerson(person)
                        .whenCompleteAsync((result, throwable) -> {
                            if (throwable != null) {
                                output.setErrorResult(throwable);
                            } else {
                                output.setResult(ResponseEntity.ok(PersonConverters.toDto(result)));
                            }
                        });
            }
        });

        return output;
    }
}
