package com.straysafe.backend.api.model.exception;

public class PetGetException extends RuntimeException {
    public PetGetException(String message) {
        super(message);
    }
}
