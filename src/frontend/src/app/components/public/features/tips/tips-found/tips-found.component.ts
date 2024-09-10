import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tips-found',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './tips-found.component.html'
})
export class TipsFoundComponent {
  constructor() {
    window.scrollTo({
      top: 0,
      behavior: "smooth"
  });
  }
}
