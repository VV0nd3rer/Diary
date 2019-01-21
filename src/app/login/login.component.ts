import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { User } from '../user';
import {ServiceResponse} from "../service-response";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user: User = {
    username: '',
    password: ''
  };
  serviceResponse: ServiceResponse<User>;

  error: boolean = false;

  constructor(private loginService: LoginService, private router: Router) {

  }


  ngOnInit() {
  }

  test(): void {
    event.preventDefault();
    this.loginService.getTest()
        .subscribe(data => this.user = data);
  }
  login(): void {
    this.loginService.login(this.user).subscribe(
      res => {
        console.log(res);
        this.serviceResponse = res;
        this.serviceResponse.responseCode != "OK" ? this.error = true :
            this.router.navigateByUrl('/');
      },
      err => { this.error = true; }
    );
  }
}
