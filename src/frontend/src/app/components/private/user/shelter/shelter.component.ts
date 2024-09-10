import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContentComponent } from '../../../public/features/search/search-reports/content/content.component';
import { Location } from '@angular/common';
import { PublicService } from '../../../../services/public.service';
import { ShelterProfileResponse, UserProfileResponse } from '../../../../interfaces/types';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'shelter',
  standalone: true,
  imports: [ContentComponent],
  templateUrl: './shelter.component.html',
})
export class ShelterComponent implements OnInit {
  route = inject(ActivatedRoute);
  publicService = inject(PublicService);
  shelterProfile: ShelterProfileResponse = {
    shelterId: '',
    shelterName: '',
    shelterAddress: '',
    shelterLatitude: '',
    shelterLongitude: '',
    userId: '',
    username: '',
    email: '',
    telephone: '',
    role: '',
    createdAt: [],
  };
  userCreatedAt!: string;

  constructor(private location: Location, private toastr: ToastrService, private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const shelterId = params.get('username') || '';
      this.publicService.getShelterByShelterId(shelterId).subscribe({
        next: (response: ShelterProfileResponse) => {
          this.shelterProfile = response;
        },
        error: (error) => {
          this.toastr.error('Failed to get shelter data', 'Error');
          // this.router.navigate(['page-404']);
        },
      });
    });
  }

  openAuthorProfile(username: string) {
    this.router.navigate([username, 'profile']);
  }

  goBack(): void {
    this.location.back();
  }
}
