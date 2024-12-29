package com.annb.quizz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RoomStartException extends RuntimeException {

    public RoomStartException(String resourceName){
        super(String.format("%s is already started", resourceName));
    }
}
