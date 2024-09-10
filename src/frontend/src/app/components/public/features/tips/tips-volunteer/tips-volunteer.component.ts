import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tips-volunteer',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './tips-volunteer.component.html'
})
export class TipsVolunteerComponent {
  constructor() {
    window.scrollTo({
      top: 0,
      behavior: "smooth"
  });
  }
}
