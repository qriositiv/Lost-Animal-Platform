package com.straysafe.backend.api.model.request;

import com.straysafe.backend.config.annotations.ValidEnum;
import com.straysafe.backend.util.enums.PetAge;
import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetSize;
import com.straysafe.backend.util.enums.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PetLostRequest(
        @NotBlank(message = "Pet name is required")
        @Pattern(regexp = "^[\\p{Alpha} ]*$", message = "Name should contain only alphabets and space")
        String petName,
        @NotNull
        @ValidEnum(enumClass = PetType.class)
        PetType petType,
        @NotNull
        @ValidEnum(enumClass = PetSize.class)
        PetSize petSize,
        @NotNull
        @ValidEnum(enumClass = PetGender.class)
        PetGender petGender,
        @NotNull
        @ValidEnum(enumClass = PetAge.class)
        PetAge petAge,
        @NotBlank(message = "Breed is required")
        String breed

) {
}
