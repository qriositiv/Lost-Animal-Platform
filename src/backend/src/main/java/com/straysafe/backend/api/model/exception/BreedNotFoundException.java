package com.straysafe.backend.api.model.exception;

public class BreedNotFoundException extends RuntimeException {
    public BreedNotFoundException(String message) {
        super(message);
    }
}