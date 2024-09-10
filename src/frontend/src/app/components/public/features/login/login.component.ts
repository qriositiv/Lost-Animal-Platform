import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { AuthService } from "../../../../services/auth.service";

import { Router, RouterLink } from "@angular/router";
import { Subscription } from "rxjs";
import { ToastrService } from "ngx-toastr";


@Component({
  selector: "app-login",
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink,
  ],
  templateUrl: "./login.component.html"
})
export class LoginComponent implements OnInit, OnDestroy {
  passwordHidden: boolean = true;
  service = inject(AuthService);
  router = inject(Router);
  loginForm!: FormGroup;
  subscription: Subscription | undefined;

  placeholderusername = $localize`:@@placeholderusername:Username*`;
  placeholderpassword = $localize`:@@placeholderpassword:Password*`;

  constructor(private formBuilder: FormBuilder, private toastr: ToastrService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      login: ["", [
        Validators.required
      ]],
      password: ["", [
        Validators.required
      ]]
    });
  }

  ngOnDestroy() {
    if (this.subscription){
      this.subscription?.unsubscribe();
    }

  }

  onSubmit() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.service.setLoginData(this.loginForm.value);
    this.subscription = this.service.loginPost().subscribe({
      next: (response) => {
        if (response.status === 200 && response.body.token) {
          this.service.setToken(response.body.token);
          this.router.navigate(["/"]);
          this.toastr.success('You are logged in!', 'Success');
        }
      },
      error: (error) => {
        if (error.status === 226) {
          this.toastr.error('Wrong login', 'Error');
        } else {
          this.toastr.error('Server side error, check console for more info', 'Error');
          console.error("There was an error!", error);
        }
        this.subscription?.unsubscribe();
      }
    });

    this.clearForm();

  }

  clearForm() {
    this.loginForm.reset();
  }

  togglePasswordVisibility() {
    this.passwordHidden = !this.passwordHidden;
  }

  get login() {
    return this.loginForm.get("login");
  }

  get password() {
    return this.loginForm.get("password");
  }


}
