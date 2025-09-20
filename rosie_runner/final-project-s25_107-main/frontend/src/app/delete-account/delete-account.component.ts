import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { environment } from "../../environments/environment.prod";

@Component({
  selector: 'app-delete-account',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './delete-account.component.html',
  styleUrls: ['./delete-account.component.css', './../user-access/user-access.component.css'],
})
export class DeleteAccountComponent implements OnInit {
  form!: FormGroup;
  pressedSubmit = false;

  readonly USERNAME_MINIMUM_LENGTH = 3;
  readonly ONLY_ALPHANUMERIC = /^[a-zA-Z0-9]+$/;

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.form = new FormGroup({
      usernameOrEmail: new FormControl('', [
        Validators.required
      ]),
      password: new FormControl('', [
        Validators.required
      ])
    });
  }

 onSubmit() {
  this.pressedSubmit = true;
  if (this.form.invalid) return;

  const data = this.form.value;

  fetch(`${environment.backendURL}/delete-account`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  })
  .then(async (response) => {
    if (response.ok) {
      console.log('Account deleted successfully');
      sessionStorage.removeItem('username');
      this.router.navigate(['/']);
    } else if (response.status === 401) {
      console.error('Invalid login information.');
      this.form.controls['usernameOrEmail'].setErrors({ invalidCredentials: true });
    } else {
      const errorData = await response.json();
      console.error('Error:', errorData?.error || 'Unknown error');
    }
  })
  .catch(err => {
    console.error('Network or server error:', err);
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
