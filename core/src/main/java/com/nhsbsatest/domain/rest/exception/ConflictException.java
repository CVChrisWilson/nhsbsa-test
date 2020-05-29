package com.nhsbsatest.domain.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// should really exist in another package
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Conflict")
public class ConflictException extends Exception {

}
