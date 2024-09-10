package com.straysafe.backend.api.model.exception;

public class PetCreateException extends RuntimeException {
    public PetCreateException(String message) {
        super(message);
    }
}
