import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { TipsComponent } from '../tips/tips-page.component';
import { StatisticComponent } from './statistic/statistic.component';
import { SearchComponent } from '../features/search/search.component';
import { Type } from '../../../enums/enums';
import { AuthService } from '../../../services/auth.service';
import { PublicService } from '../../../services/public.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faL, faShieldDog } from '@fortawesome/free-solid-svg-icons';
import { ModeratorDashboardComponent } from '../../private/moderator-dashboard/moderator-dashboard.component';
import { ShelterProfileResponse } from '../../../interfaces/types';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'home',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TipsComponent,
    StatisticComponent,
    SearchComponent,
    FontAwesomeModule,
    ModeratorDashboardComponent,
  ],
  templateUrl: './home-page.component.html',
})
export class HomeComponent {
  Type = Type;
  constructor() {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }

  authService = inject(AuthService);
  publicService = inject(PublicService);
  toastr = inject(ToastrService);
  router = inject(Router);
  isShelter: boolean = false;
  isModerator: boolean = false;
  username: string = '';
  faShield = faShieldDog;

  openReports() {
    this.router.navigate([this.username, 'shelter']);
  }

  sendShelterToPetSelection() {
    const token = this.authService.decodeToken();
    if (!token) return;
    if (token.role === 'SHELTER') {
      this.publicService.getShelterByShelterId(token.username).subscribe({
        next: (response: ShelterProfileResponse) => {
          this.router.navigate(['/new-report'], {
            queryParams: {
              type: Type.Found,
              step: 'pet-selection',
              address: response.shelterAddress,
            },
          });
        },
        error: (error) => {
          this.toastr.error('Failed to get shelter data', 'Error');
          console.error(error);
          // this.router.navigate(['page-404']);
        },
      });
    }
  }

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (!token) return;
    this.isShelter = token.role === 'SHELTER';
    this.isModerator = token.role === 'MODERATOR';
    this.username = token.username;
  }
}
