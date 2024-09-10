import { Component, inject, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { Subscription } from "rxjs";
import { AuthService } from "../../../../services/auth.service";
import { ToastrService } from "ngx-toastr";


@Component({
  selector: "app-register",
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: "./register.component.html"
})
export class RegisterComponent implements OnInit, OnDestroy {
  passwordHidden: boolean = true;
  registerForm!: FormGroup;
  service = inject(AuthService);
  router = inject(Router);
  subscription: Subscription | undefined;

  placeholderusername = $localize`:@@placeholderusername:Username*`;
  placeholderpassword = $localize`:@@placeholderpassword:Password*`;
  placeholderemail = $localize`:@@placeholderemail:Email*`;
  placeholderfirstname = $localize`:@@placeholderfirstname:First name*`;
  placeholderlastname = $localize`:@@placeholderlastname:Last name*`;
  placeholderphonenumber = $localize`:@@placeholderphonenumber:Phone number*`;

  constructor(private formBuilder: FormBuilder, private toastr: ToastrService) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      login: ["", [
        Validators.required,
        Validators.pattern("^[a-zA-Z0-9]+$"),
        Validators.minLength(6),
        Validators.maxLength(20)
      ]],
      password: ["", [
        Validators.required,
        Validators.pattern("^(?!.*\\s)(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
      ]],
      email: ["", [
        Validators.required,
        Validators.pattern("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")]],
      firstName: ["", [
        Validators.required,
        Validators.maxLength(60),
        Validators.pattern("^[a-zA-Z]+$")
      ]],
      lastName: ["", [
        Validators.required,
        Validators.maxLength(60),
        Validators.pattern("^[a-zA-Z]+$")
      ]],
      phone: ["", [
        Validators.required,
        Validators.pattern("^\\+([0-9]{7,14})$")
      ]]
    });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  onSubmit() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.service.setRegisterData(this.registerForm.value);
    this.subscription = this.service.registerPost().subscribe({
      next: (data) => {
        if (data.status === 201 && data.body.token) {
          this.service.setToken(data.body.token);
          this.router.navigate(["/"]);
          this.toastr.success('You have successfully registered.', 'Welcome!')
        }
      },
      error: (error) => {
        console.error("There was an error!", error);
        this.toastr.error(error.error.text, 'Error')
        this.subscription?.unsubscribe();
      }
    });
  }

  togglePasswordVisibility() {
    this.passwordHidden = !this.passwordHidden;
  }

  get login() {
    return this.registerForm.get("login");
  }

  get password() {
    return this.registerForm.get("password");
  }

  get email() {
    return this.registerForm.get("email");
  }

  get firstName() {
    return this.registerForm.get("firstName");
  }

  get lastName() {
    return this.registerForm.get("lastName");
  }

  get phone() {
    return this.registerForm.get("phone");
  }

}

