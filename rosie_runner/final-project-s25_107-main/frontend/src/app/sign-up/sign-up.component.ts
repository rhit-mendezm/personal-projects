import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse} from '@angular/common/http';
import { UserAccessComponent } from '../user-access/user-access.component';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css', './../user-access/user-access.component.css']
})
export class SignUpComponent extends UserAccessComponent {
  override name = 'sign-up';
  private subscriptions: Subscription[] = [];

  constructor(http: HttpClient, router: Router) {
    super(http, router);
  }

  ngOnInit() {
    this.setupFormValidation();
    this.setupSubscribers();
  }

  setupFormValidation() {
    this.form = new FormGroup({
      username: new FormControl('', 
      [
        Validators.required, 
        Validators.minLength(this.USERNAME_MINIMUM_LENGTH),
        Validators.maxLength(this.USERNAME_MAXIMUM_LENGTH),
        Validators.pattern(this.ONLY_ALPHANUMERIC)
      ]),

      email: new FormControl('', 
      [
        Validators.required, 
        Validators.email
      ]),

      password: new FormControl('',
      [
        Validators.required, 
        Validators.minLength(this.PASSWORD_MINIMUM_LENGTH),
        Validators.pattern(this.ONE_LOWERCASE_ONE_UPPERCASE_ONE_NUMBER)
      ]),

      confirmPassword: new FormControl('', [Validators.required]),

      major: new FormControl('', [Validators.required])
    },
  
    { validators: this.verifyPasswordsMatch });
  }

  setupSubscribers() {
    this.setupUsernameSubscriber();
    this.setupEmailSubscriber();
  }

  setupUsernameSubscriber() {
    const usernameSubscription = this.form.get('username')?.valueChanges.subscribe( () => {
      const usernameField = this.form.get('username');
      if (usernameField?.hasError('usernameTaken')) {
        const errors = usernameField.errors;
        if (errors) {
          delete errors['usernameTaken'];
          usernameField.setErrors(errors);
        }
      }
    });

    if (usernameSubscription) {
      this.subscriptions.push(usernameSubscription);
    }
  }

  setupEmailSubscriber() {
    const emailSubscription = this.form.get('email')?.valueChanges.subscribe( () => {
      const emailField = this.form.get('email');
      if (emailField?.hasError('emailTaken')) {
        const errors = emailField.errors;
        if (errors) {
          delete errors['emailTaken'];
          emailField.setErrors(errors);
        }
      }
    });

    if (emailSubscription) {
      this.subscriptions.push(emailSubscription);
    }
  }

  verifyPasswordsMatch(form: AbstractControl) {
    const passwordField = form.get('password');
    const confirmPasswordField = form.get('confirmPassword');

    if (!passwordField || !confirmPasswordField) {
      return null;
    }

    const password = passwordField.value;
    const confirmPassword = confirmPasswordField.value;

    if (password !== confirmPassword) {
      confirmPasswordField.setErrors({ passwordsMismatch: true });
    }
    else {
      return null;
    }

    return null;
  }

  handleServerError(error: HttpErrorResponse) {
    if (error.status === 400) {
      console.error('Bad request. Please check your input.');
    } else if (error.status === 409 && error.error.code === 'email_already_taken') {
      console.error('Email already exists. Please choose a different one.');
      this.form.controls['email'].setErrors({ emailTaken: true });    
    } else if (error.status === 409 && error.error.code === 'username_already_taken') {
      console.error('Username already exists. Please choose a different one.');
      this.form.controls['username'].setErrors({ usernameTaken: true });
    } else {
      console.error('An unexpected error occurred:', error);
    }
  }
}
