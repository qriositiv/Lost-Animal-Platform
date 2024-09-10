package com.straysafe.backend.domain;

import com.straysafe.backend.api.model.request.GridviewLocationRequest;

import java.util.List;

public record GridviewReportDAORequest (
        Long page,
        List<String> reportTypeList,
        GridviewLocationRequest reportLocationRequest,
        Integer reportPublishHourRange,
        GridviewPetDataDAORequest petDataRequest,
        String reportCreator,
        String searchQuery
){
}
