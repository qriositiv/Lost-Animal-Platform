import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-pet-summary',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './pet-summary.component.html'
})
export class PetSummaryComponent {

}
