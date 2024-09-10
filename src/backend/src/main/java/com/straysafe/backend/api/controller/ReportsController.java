package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.request.GridviewReportRequest;
import com.straysafe.backend.api.model.request.ReportRequest;
import com.straysafe.backend.api.model.response.ListReportResponse;
import com.straysafe.backend.api.model.response.ReportResponse;
import com.straysafe.backend.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReportsController {

  private final ReportService reportService;

  public ReportsController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/public/reports")
  @ResponseStatus(HttpStatus.OK)
  public List<ListReportResponse> getAllReports() {
    return reportService.getAllReports();
  }

  @PostMapping("/public/reports/filter")
  @ResponseStatus(HttpStatus.OK)
  public List<ListReportResponse> getReportsByFilter(@RequestBody GridviewReportRequest request) {
    return reportService.getAllReportsFiltered(request);
  }

  @GetMapping("/public/reports/user/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<ListReportResponse> getAllReportsByUserId(@PathVariable String userId) {
    return reportService.getAllReportsByUserId(userId);
  }

  @GetMapping("/public/reports/{reportId}")
  @ResponseStatus(HttpStatus.OK)
  public ReportResponse getReportById(@PathVariable String reportId) {
    return reportService.getReportById(reportId);
  }

  @PostMapping("/api/reports/create")
  @ResponseStatus(HttpStatus.CREATED)
  public void createReport(@Valid @RequestBody ReportRequest report) {
    reportService.createReport(report);
  }

  @DeleteMapping("/api/reports/delete")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteReport(@RequestParam String reportId) {
    reportService.deleteReport(reportId);
  }

  @PatchMapping("/api/reports/status/update/{reportId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateReport(@PathVariable String reportId) {
    reportService.updateReport(reportId);
  }

  @PatchMapping("/api/reports/status/block/{reportId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void blockReport(@PathVariable String reportId) {
    reportService.blockReport(reportId);
  }
}
