package com.straysafe.backend.api.model.request;

import com.straysafe.backend.config.annotations.ValidEnum;
import com.straysafe.backend.util.enums.PetAge;
import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetSize;
import com.straysafe.backend.util.enums.PetType;
import jakarta.validation.constraints.NotNull;

public record PetSeenRequest(
        @NotNull(message = "isTaken is required")
        Boolean isTaken,
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
        String breed
) {
}
