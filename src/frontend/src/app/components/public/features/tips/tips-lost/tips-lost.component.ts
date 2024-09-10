import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-tips-lost',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './tips-lost.component.html'
})
export class TipsLostComponent {
  constructor() {
    window.scrollTo({
      top: 0,
      behavior: "smooth"
  });
  }
}
