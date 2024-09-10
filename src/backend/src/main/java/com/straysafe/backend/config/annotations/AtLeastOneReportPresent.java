package com.straysafe.backend.config.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = AtLeastOneReportValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface AtLeastOneReportPresent {
    String message() default "Either PetLostRequest or PetSeenRequest must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
