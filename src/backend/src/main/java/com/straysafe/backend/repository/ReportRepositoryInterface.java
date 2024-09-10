package com.straysafe.backend.repository;

import com.straysafe.backend.domain.ReportDAORequest;
import com.straysafe.backend.domain.ReportDAOResponse;
import com.straysafe.backend.domain.SimilarityReportDAORequest;
import com.straysafe.backend.domain.SimilarityReportDAOResponse;

import java.util.List;

public interface ReportRepositoryInterface {
    void createReport(ReportDAORequest reportRequest);

    List<ReportDAOResponse> getAllReports();

    String getOwnerByReportId(String reportId);

    void deleteReportById(String reportId);

    ReportDAOResponse getReportById(String reportId);

    void updateReportById(String reportId);

    List<SimilarityReportDAOResponse> getSimilarReportList(SimilarityReportDAORequest similarReportDAORequest);
}
