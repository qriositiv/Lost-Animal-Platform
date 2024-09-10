import { inject, Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { from, Observable, switchMap } from 'rxjs';
import { FormGroup } from '@angular/forms';
import { Type } from '../enums/enums';

@Injectable({
  providedIn: 'root',
})
export class NewPetService {
  private http = inject(HttpClient);
  private springApiUrl = environment.springApiUrl;

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

  createPet(data: FormGroup, type: Type): Observable<string> {
    let blobUrl = data.get('image')?.value;

    return from(fetch(blobUrl).then((response) => response.blob())).pipe(
      switchMap((blob) => this.blobToBase64(blob)),
      switchMap((base64String) => {
        base64String = base64String.replace('data:image/png;base64,', '');

        const petData =
          type === Type.Lost
            ? {
                petLostRequest: {
                  petName: data.get('pet_name')?.value,
                  petType: data.get('pet_type')?.value,
                  petSize: data.get('pet_size')?.value,
                  petGender: data.get('pet_gender')?.value,
                  petAge: data.get('pet_age')?.value,
                  breed: data.get('pet_breed')?.value,
                },
                image: base64String,
              }
            : {
                petSeenRequest: {
                  isTaken: data.get('pet_found')?.value,
                  petType: data.get('pet_type')?.value,
                  petSize: data.get('pet_size')?.value,
                  petGender: data.get('pet_gender')?.value,
                  petAge: data.get('pet_age')?.value,
                  breed: data.get('pet_breed')?.value,
                },
                image: base64String,
              };

        const headers = new HttpHeaders({
          'Content-Type': 'application/json',
        });

        return this.http.post<any>(`${this.springApiUrl}/api/pet/create`, petData, {
          headers,
          responseType: 'text' as 'json',
        });
      }),
    );
  }
}
