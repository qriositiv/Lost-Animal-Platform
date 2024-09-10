package com.straysafe.backend.api.model.request;

import com.straysafe.backend.config.annotations.AtLeastOneReportPresent;
import jakarta.validation.Valid;

@AtLeastOneReportPresent
public record PetRequest(
        @Valid
        PetLostRequest petLostRequest,
        @Valid
        PetSeenRequest petSeenRequest,
        String image
){
}
