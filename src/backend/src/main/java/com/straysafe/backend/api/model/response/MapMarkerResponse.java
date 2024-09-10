package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.PetType;
import com.straysafe.backend.util.enums.ReportType;

import java.math.BigDecimal;

public record MapMarkerResponse (
    String report_id,
    BigDecimal latitude,
    BigDecimal longitude,
    ReportType report_type,
    PetType animal_type
){
}
