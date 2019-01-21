import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationForm } from '../registration-form';
import { RegistrationService } from '../registration.service';
import {ServiceResponse} from "../service-response";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  registrationForm: RegistrationForm = {
    username: '',
    password: '',
    matchingPassword: '',
    email: ''
  };
  serviceResponse: ServiceResponse<any>;
  error: boolean = false;

  constructor(private registrationService: RegistrationService, private router: Router) { }

  ngOnInit() {

  }
  register(): void {
    this.registrationService.register(this.registrationForm).subscribe(
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
