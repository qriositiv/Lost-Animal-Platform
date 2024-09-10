package com.straysafe.backend.api.model.response;

public record UserReportInfoResponse (
    String userId,
    String username,
    String firstName,
    String lastName,
    String email,
    String telephone) {}
