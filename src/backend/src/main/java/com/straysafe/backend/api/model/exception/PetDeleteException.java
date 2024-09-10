package com.straysafe.backend.api.model.exception;

public class PetDeleteException extends RuntimeException {
    public PetDeleteException(String message) {
        super(message);
    }
}
