import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, from, Observable, shareReplay, switchMap, throwError, timeout } from "rxjs";
import { environment } from '../../environment/environment';
import { MapResponse } from '../interfaces/marker.interface';
import { Pet } from '../interfaces/pet.interface';
import { StatisticResponse, UserProfileResponse, ShelterProfileResponse } from '../interfaces/types';
import {
  ReportRequestWithSimilarity,
  ReportResponse,
  ReportResponseWithSimilarity,
  SingleReportResponse,
} from '../interfaces/report.interface';
import { CommentResponse } from '../interfaces/comment.interface';

@Injectable({
  providedIn: 'root',
})
export class PublicService {
  private springApiUrl = environment.springApiUrl;
  private libretranslateApiUrl = environment.libretranslateApiUrl;
  private client = inject(HttpClient);

  getStatistic(): Observable<StatisticResponse> {
    return this.client.get<StatisticResponse>(`${this.springApiUrl}/public/statistic`);
  }

  getProfileByUsername(username: string): Observable<UserProfileResponse> {
    return this.client.get<UserProfileResponse>(`${this.springApiUrl}/public/users/${username}/profile`);
  }

  getShelterByShelterId(shelterId: string): Observable<ShelterProfileResponse> {
    return this.client.get<ShelterProfileResponse>(`${this.springApiUrl}/public/shelters/${shelterId}/profile`);
  }

  getReportMarker(): Observable<MapResponse> {
    return this.client.get<MapResponse>(`${this.springApiUrl}/public/marker`);
  }

  getPetListByUsername(username: string): Observable<Pet[]> {
    return this.client.get<Pet[]>(`${this.springApiUrl}/public/pet/${username}`).pipe(
      catchError((error) => throwError(() => error)),
      shareReplay(1)
    );
  }

  getFilteredReports(filterParams: any): Observable<Array<ReportResponse>> {
    return this.client.post<Array<ReportResponse>>(`${this.springApiUrl}/public/reports/filter`, filterParams);
  }

  deletePet(petId: string): Observable<any> {
    return this.client.delete<any>(`${this.springApiUrl}/api/pet/delete/${petId}`);
  }

  getReportById(reportId: string): Observable<SingleReportResponse> {
    return this.client.get<SingleReportResponse>(`${this.springApiUrl}/public/reports/${reportId}`);
  }

  getAllReports(): Observable<Array<ReportResponse>> {
    return this.client.get<Array<ReportResponse>>(`${this.springApiUrl}/public/reports`);
  }

  getAllReportsByUserId(userId: string): Observable<Array<ReportResponse>> {
    return this.client.get<Array<ReportResponse>>(`${this.springApiUrl}/public/reports/user/${userId}`);
  }

  getAllCommentsByReportId(reportId: string): Observable<Array<CommentResponse>> {
    return this.client.get<Array<CommentResponse>>(`${this.springApiUrl}/public/comments/${reportId}`);
  }

  searchAddress(address: string): Observable<any> {
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`;
    return this.client.get<any>(url);
  }

  translate(q: string, source: string, target: string): Observable<any> {
    const json = {
      q: q,
      source: source,
      target: target,
      format: 'text',
    };

    return this.client.post<any>(`${this.libretranslateApiUrl}/translate`, json);
  }

  private blobToBase64(blob: Blob): Observable<string> {
    return new Observable((observer) => {
      const reader = new FileReader();
      reader.onloadend = () => {
        observer.next(reader.result as string);
        observer.complete();
      };
      reader.onerror = (error) => observer.error(error);
      reader.readAsDataURL(blob);
    });
  }

  quickCompare(req: ReportRequestWithSimilarity): Observable<Array<ReportResponseWithSimilarity>> {
    const blobUrl = req.image;

    return from(fetch(blobUrl).then((response) => response.blob())).pipe(
      switchMap((blob) => this.blobToBase64(blob)),
      switchMap((base64String) => {
        base64String = base64String.replace('data:image/png;base64,', '');
        req.image = base64String;
        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
        });
        const options = {
          headers: headers,
          timeout: 30000,
        };


        return this.client.post<Array<ReportResponseWithSimilarity>>(
          `${this.springApiUrl}/public/image/comparison`,
          req,
          options
        );
      })
    );
  }
}
