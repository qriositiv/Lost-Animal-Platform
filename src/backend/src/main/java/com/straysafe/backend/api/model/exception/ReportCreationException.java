package com.straysafe.backend.api.model.exception;

public class ReportCreationException extends RuntimeException {
    public ReportCreationException(String message) {
        super(message);
    }
}