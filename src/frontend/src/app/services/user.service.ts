import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { catchError, Observable, throwError } from 'rxjs';
import { CreateCommentRequest } from '../interfaces/comment.interface';
import { ProfileUpdateRequest } from '../interfaces/types';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private springApiUrl = environment.springApiUrl;
  private client = inject(HttpClient);

  postCommentByReportId(reportId: string, comment: string): Observable<any> {
    return this.client
      .post<CreateCommentRequest>(`${this.springApiUrl}/api/comments/post`, {
        reportId: reportId,
        comment: comment,
      })
      .pipe(catchError((error) => throwError(() => error)));
  }

  deleteCommentByReportId(reportId: string, commentId: string) {
    return this.client
      .delete(`${this.springApiUrl}/api/comments/delete`, {
        params: {
          reportId: reportId,
          commentId: commentId,
        },
      })
      .pipe(catchError((error) => throwError(() => error)));
  }

  updateProfile(profileUpdateRequest: ProfileUpdateRequest): Observable<void> {
    return this.client.put<void>(`${this.springApiUrl}/api/users/profile/update`, profileUpdateRequest);
  }

  updateSecurityAndOther(profileSecurityUpdateRequest: any): Observable<void> {
    return this.client.put<void>(`${this.springApiUrl}/api/users/security/update`, profileSecurityUpdateRequest);
  }

  blockUser(userId: string): Observable<void> {
    const url = `${this.springApiUrl}/api/users/security/block`;
    return this.client.put<void>(url, userId);
  }
}
