import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'tips',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './tips-page.component.html'
})
export class TipsComponent {
  constructor() {
    window.scrollTo({
      top: 0,
      behavior: "smooth"
  });
  }
}
