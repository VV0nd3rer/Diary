import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { User } from '../user';
import {LoginStatus} from "../user";
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
  loginStatus: LoginStatus = {
    code: '',
    message: ''
  };

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
        this.loginStatus = res;
        this.loginStatus.code == "NO_USER_WITH_USERNAME" ? this.error = true :
            this.router.navigateByUrl('/');
      },
      err => { this.error = true; }
    );
  }

}
