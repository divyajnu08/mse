package com.virtue.csr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.virtue.csr.dto.Status;
import com.virtue.csr.exception.ApplicationException;

public abstract class AbstractService {

    @Autowired
    private SequenceGenerator generator;

    public long getNextSequence(final String key) throws ApplicationException {
        return generator.getNextSequenceId(key);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Status> handleException(Exception e) {
        e.printStackTrace();
        final Status status;
        if(e instanceof ApplicationException) {
            status = new Status(e.getMessage(), "Server Error");
        } else {
            status = new Status("Internal Application Error", "Server Error");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
    }

}
