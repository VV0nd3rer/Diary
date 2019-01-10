import { Component, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { User } from '../user';
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



  constructor(private loginService: LoginService) {

  }


  ngOnInit() {
  }

  test(): void {
    event.preventDefault();
    this.loginService.getTest()
        .subscribe(data => this.user = data);
  }
  login(): void {
    this.loginService.login(this.user).subscribe();
  }

}
