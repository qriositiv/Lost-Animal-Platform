package com.straysafe.backend.api.model.response;

public record StatisticResponse (

        long userCount,
        long shelterCount,
        long reportCount,
        long foundReportCount
){}

