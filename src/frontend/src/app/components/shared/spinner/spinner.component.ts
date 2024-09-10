import { Component, inject } from '@angular/core';
import { LoaderService } from '../../../services/loader.service';

@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [],
  templateUrl: './spinner.component.html',
  styleUrl: './spinner.component.css',
})
export class SpinnerComponent {
  loaderService = inject(LoaderService);
}
