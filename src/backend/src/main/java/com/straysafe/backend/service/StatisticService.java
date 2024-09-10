package com.straysafe.backend.service;

import com.straysafe.backend.api.model.response.StatisticResponse;
import com.straysafe.backend.repository.StatisticRepository;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public StatisticResponse getAllStatistics() {
        long userCount = statisticRepository.getUserCount();
        long shelterCount = statisticRepository.getShelterCount();
        long reportCount = statisticRepository.getReportCount();
        long foundReportCount = statisticRepository.getFoundReportCount();

        return new StatisticResponse(
                userCount,
                shelterCount,
                reportCount,
                foundReportCount
        );
    }

}
