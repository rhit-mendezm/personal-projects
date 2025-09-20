import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse} from '@angular/common/http';
import { UserAccessComponent } from '../user-access/user-access.component';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-log-in',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css', './../user-access/user-access.component.css'],
})
export class LogInComponent extends UserAccessComponent {
  override name = 'log-in';

  constructor(http: HttpClient, router: Router) {
    super(http, router);
  }

  ngOnInit() {
    this.form = new FormGroup({
      usernameOrEmail: new FormControl('', 
      [
        Validators.required
      ]),

      password: new FormControl('', 
      [
        Validators.required 
      ])
    });
  }

  handleServerError(error: HttpErrorResponse) {
    if (error.status === 400) {
      console.error('Bad request. Please check your input.');
    } else if (error.status === 401) {
      console.error('Login information is incorrect. Please try again.');
      this.form.setErrors({ invalidCredentials: true });
    } else {
      console.error('An unexpected error occurred:', error);
    }
  }
}
