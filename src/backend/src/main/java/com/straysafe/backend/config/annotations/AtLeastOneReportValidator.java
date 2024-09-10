package com.straysafe.backend.config.annotations;

import com.straysafe.backend.api.model.request.PetRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneReportValidator implements ConstraintValidator<AtLeastOneReportPresent, PetRequest> {
    @Override
    public boolean isValid(PetRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.petLostRequest() != null || value.petSeenRequest() != null;
    }
}
