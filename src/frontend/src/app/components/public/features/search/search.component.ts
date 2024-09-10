import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'search',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './search.component.html'
})
export class SearchComponent {
  constructor() {
    window.scrollTo({
      top: 0,
      behavior: "smooth"
    });
  }
}
