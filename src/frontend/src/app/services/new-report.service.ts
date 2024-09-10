import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { FormGroup } from '@angular/forms';
import { catchError, Observable, throwError } from 'rxjs';
import { ReportFormRequest } from '../interfaces/report.interface';

@Injectable({
  providedIn: 'root',
})
export class NewReportService {
  private client = inject(HttpClient);
  private springApiUrl = environment.springApiUrl;

  createReport(formData: FormGroup): Observable<any> {
    return this.client
      .post<ReportFormRequest>(`${this.springApiUrl}/api/reports/create`, {
        reportType: formData.get('report_type')?.value,
        longitude: formData.get('longitude')?.value,
        latitude: formData.get('latitude')?.value,
        address: formData.get('address')?.value,
        petId: formData.get('pet_id')?.value,
        note: formData.get('report_notes')?.value,
      })
      .pipe(catchError((error) => throwError(() => error)));
  }

  updateReport(reportId: string): Observable<any> {
    return this.client
      .patch(`${this.springApiUrl}/api/reports/status/update/${reportId}`, {})
      .pipe(catchError((error) => throwError(() => error)));
  }

  deleteReport(reportId: string): Observable<void> {
    return this.client
      .delete<void>(`${this.springApiUrl}/api/reports/delete?reportId=${reportId}`)
      .pipe(catchError((error) => throwError(() => error)));
  }

  blockReport(reportId: string): Observable<void> {
    const url = `${this.springApiUrl}/api/reports/status/block/${reportId}`;
    return this.client.patch<void>(url, null);
  }
}
