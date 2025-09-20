import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { environment } from "../../environments/environment.prod";

@Component({
    template: '',
})
export abstract class UserAccessComponent implements OnInit {
    readonly ONE_LOWERCASE_ONE_UPPERCASE_ONE_NUMBER = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/;
    readonly ONLY_ALPHANUMERIC = /^[a-zA-Z0-9]+$/;
    readonly USERNAME_MINIMUM_LENGTH = 3;
    readonly USERNAME_MAXIMUM_LENGTH = 10;
    readonly PASSWORD_MINIMUM_LENGTH = 8;
    
    name!: string;
    form!: FormGroup;
    pressedSubmit = false;

    constructor(private http: HttpClient, private router: Router) { }

    abstract ngOnInit(): void;

    onSubmit() {
        this.pressedSubmit = true;
        if (this.form.valid) {
            const data = this.form.value;
            this.http
                .post(`${environment.backendURL}/${this.name}`, data, { observe: 'response' })
                .subscribe({
                    next: (response) => {
                        console.log(`${this.name} response: `, response);
                        sessionStorage.setItem('username', data.username);
                        this.router.navigate(['/game']);
                    },
                    error: (error) => {
                        this.handleServerError(error);
                    }
                });
        } else {
            console.error(`${this.name} form is invalid.`);
        }
    }

    abstract handleServerError(error: HttpErrorResponse): void
}
