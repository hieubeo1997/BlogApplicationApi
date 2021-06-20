package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnAuthorizeException extends RuntimeException {
    public UnAuthorizeException(String message){
        super(message);
    }
}
