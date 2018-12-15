import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
    templateUrl: './login.component.html'
})
export class LoginComponent {

    credentials = {username: '', password: ''};

    constructor(private app: AppService, private http: HttpClient, private router: Router) {
    }

    login(): void {
        this.app.login(this.credentials).subscribe();
    }

}