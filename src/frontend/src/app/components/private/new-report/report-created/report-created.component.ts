import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../../services/auth.service';
import { PublicService } from '../../../../services/public.service';

@Component({
  selector: 'app-report-created',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './report-created.component.html',
})
export class ReportCreatedComponent {
  constructor() {
    window.scrollTo({ top: 0 });
  }

  username!: string;
  authService = inject(AuthService);
  publicService = inject(PublicService);

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (!token) return;

    const username = token.username;
    this.publicService.getProfileByUsername(username).subscribe({
      next: (response: any) => {
        this.username = response.username;
      },
      error: (error) => {
        console.error('ERROR FETCHING USERNAME:', error);
      },
    });
  }
}
