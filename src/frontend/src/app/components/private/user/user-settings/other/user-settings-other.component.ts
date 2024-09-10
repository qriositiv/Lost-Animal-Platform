import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../../../services/user.service';
import { AuthService } from '../../../../../services/auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-settings-other',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './user-settings-other.component.html',
})
export class UserSettingsOtherComponent implements OnInit {
  securityAndOtherForm!: FormGroup;
  passwordHidden: boolean = true;
  service = inject(UserService);
  authService = inject(AuthService);
  currentRole!: string;

  constructor(private formBuilder: FormBuilder, private toastr: ToastrService) {}

  ngOnInit(): void {
    const token = this.authService.decodeToken();
    if (!token) return;
    this.currentRole = token.role;
    this.securityAndOtherForm = this.formBuilder.group({
      password: [
        '',
        [
          Validators.required,
          Validators.pattern('^(?!.*\\s)(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$'),
        ],
      ],
    });
  }

  get password() {
    return this.securityAndOtherForm.get('password');
  }

  togglePasswordVisibility(): void {
    this.passwordHidden = !this.passwordHidden;
  }

  onUpdateSecurity(): void {
    if (this.securityAndOtherForm.valid) {
      this.service.updateSecurityAndOther(this.securityAndOtherForm.value).subscribe({
        next: () => {
          this.toastr.success('Security settings updated successfully', 'Success');
        },
        error: () => {
          this.toastr.error('An error occurred while updating security settings', 'Error');
        },
      });
    } else {
      this.toastr.error('Please fix the errors in the form', 'Error');
    }
  }
}
