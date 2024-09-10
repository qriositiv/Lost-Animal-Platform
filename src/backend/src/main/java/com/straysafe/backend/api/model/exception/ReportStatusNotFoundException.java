package com.straysafe.backend.api.model.exception;

public class ReportStatusNotFoundException extends RuntimeException {
    public ReportStatusNotFoundException(String message) {
        super(message);
    }
}