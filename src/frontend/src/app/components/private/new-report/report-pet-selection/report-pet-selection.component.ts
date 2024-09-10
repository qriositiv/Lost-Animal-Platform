import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { UserPetsComponent } from '../../user/user-pets/user-pets.component';
import { Type } from '../../../../enums/enums';
import { RouterLink } from '@angular/router';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PrivacyPolicyComponent } from './privacy-policy/privacy-policy.component';
import { TranslateService } from '../../../../services/translate.service';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-report-pet-selection',
  standalone: true,
  imports: [UserPetsComponent, RouterLink, ReactiveFormsModule, PrivacyPolicyComponent, CommonModule],
  templateUrl: './report-pet-selection.component.html',
  styleUrl: './report-pet-selection.component.css',
})
export class ReportPetSelectionComponent implements OnInit {
  @Input() form!: FormGroup;
  @Output() buttonClicked: EventEmitter<any> = new EventEmitter<any>();
  Type = Type;

  authService = inject(AuthService);
  pet_selected: boolean = false;
  petId: string = '';
  showPrivacyPolicy: boolean = false;

  translate = inject(TranslateService);

  onSubmit() {
    this.buttonClicked.emit();
  }

  ngOnInit(): void {
    const token = this.authService.decodeToken();
    if (!token) return;
    if(token.role === 'SHELTER') {
      
    }
  }

  handlePetSelected($event: any) {
    if (this.pet_selected && this.petId === $event) {
      this.form.patchValue({
        pet_selected: false,
        pet_id: '',
      });
    } else {
      this.form.patchValue({
        pet_selected: true,
        pet_id: $event,
      });
    }
  }

  onClosePreview() {
    this.showPrivacyPolicy = false;
  }
  openPrivacyPolicy() {
    this.showPrivacyPolicy = true;
  }

  public get reportType(): Type {
    return this.form.get('report_type')?.value;
  }

  public get reportNotes(): string {
    return this.form.get('report_notes')?.value;
  }
}
