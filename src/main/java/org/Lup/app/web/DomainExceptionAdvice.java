package org.Lup.app.web;

import org.Lup.app.exception.DomainException;
import org.Lup.app.web.response.DomainErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DomainExceptionAdvice {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<DomainErrorResponse> handleException(DomainException ex){
        DomainErrorResponse body = new DomainErrorResponse();
        body.setMessage(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
