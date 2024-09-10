import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Type } from '../../../enums/enums';
import { NewPetComponent } from '../new-pet/new-pet.component';
import { Location } from '../../../interfaces/location.interface';
import { ReportPetSelectionComponent } from './report-pet-selection/report-pet-selection.component';
import { AddressInputComponent } from '../../shared/address-input/address-input.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NewReportService } from '../../../services/new-report.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { PublicService } from '../../../services/public.service';

@Component({
  selector: 'app-new-report',
  standalone: true,
  imports: [CommonModule, AddressInputComponent, NewPetComponent, ReportPetSelectionComponent],
  templateUrl: './new-report.component.html',
})
export class NewReportComponent implements OnInit, OnDestroy {
  currentStep: string = 'location';
  reportForm: FormGroup;
  subscription: Subscription | undefined;

  router = inject(Router);
  toastr = inject(ToastrService);
  publicService = inject(PublicService);

  constructor(private reportService: NewReportService, private fb: FormBuilder, private route: ActivatedRoute) {
    this.reportForm = this.fb.group({
      report_type: [Type.Lost, [Validators.required]],
      address: '',
      latitude: 0,
      longitude: 0,
      pet_selected: [false, [Validators.requiredTrue]],
      pet_id: [null, [Validators.required]],
      report_notes: ['', [Validators.maxLength(500)]],
      report_contacts: [false, [Validators.requiredTrue]],
      report_policy: [false, [Validators.requiredTrue]],
    });
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.reportForm.patchValue({
        report_type: <Type>params['type'],
      });
      this.currentStep = params['step'] || 'location';
      const address = params['address'];
      if (address) {
        this.publicService.searchAddress(address).subscribe({
          next: (response) => {
            const displayName = response[0].display_name;
            const addressComponents: string[] = displayName.split(',').map((component: string) => component.trim());
            const shortAddress = addressComponents.slice(0, 3).join(', ');

            this.reportForm.patchValue({
              address: shortAddress,
              longitude: response[0].lon,
              latitude: response[0].lat,
            });
          },
          error: (error) => {
            console.error(error);
          },
        });
      }
    });
  }
  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  getReportType(): Type {
    return this.reportForm.get('report_type')?.value;
  }

  getLocationData(location: Location) {
    this.reportForm.patchValue({
      address: location.address,
      latitude: location.latitude,
      longitude: location.longitude,
    });
  }

  onSubmit() {
    this.subscription = this.reportService.createReport(this.reportForm).subscribe({
      next: (response) => {
        this.toastr.success('You have sucessfully created a new report!', 'Success!');
        this.router.navigate(['/submitted']);
      },
      error: (err) => {
        console.error('Error:', err);
      },
    });
  }
}
