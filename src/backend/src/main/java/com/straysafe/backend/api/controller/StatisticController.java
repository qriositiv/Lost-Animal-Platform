package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.response.StatisticResponse;
import com.straysafe.backend.service.StatisticService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/public/statistic")
    @ResponseStatus(HttpStatus.OK)
    public StatisticResponse getStatistics() {
        return statisticService.getAllStatistics();
    }

}
