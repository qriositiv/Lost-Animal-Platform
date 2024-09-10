package com.straysafe.backend.config.annotations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean isValid = false;
        StringBuilder validValues = new StringBuilder();

        for (Enum<?> enumConst : enumClass.getEnumConstants()) {
            validValues.append(enumConst.name()).append(", ");
            if (enumConst.name().equals(value.name())) {
                isValid = true;
            }
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
                            .replace("{validValues}", validValues.toString().replaceAll(", $", "")))
                    .addConstraintViolation();
        }

        return isValid;
    }
}
