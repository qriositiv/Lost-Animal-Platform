package com.straysafe.backend.api.model.exception;

public class PetTypeNotFoundException extends RuntimeException {
    public PetTypeNotFoundException(String message) {
        super(message);
    }
}