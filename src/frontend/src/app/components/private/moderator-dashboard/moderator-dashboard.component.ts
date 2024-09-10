import { Component, OnInit, inject } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-moderator-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './moderator-dashboard.component.html',
})
export class ModeratorDashboardComponent implements OnInit {
  authService = inject(AuthService);
  username: string = '';

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (!token) return;
    this.username = token.username;
  }
}
