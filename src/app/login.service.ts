import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Observer, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User, LoginStatus } from './user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  isLoggedIn: Observable<boolean>;
  private observer: Observer<boolean>;

    greeting = {};
  constructor(private http: HttpClient) {
    this.isLoggedIn = new Observable(observer => this.observer = observer);

  }
    getTest() : Observable<User> {
        return this.http.get<User>("test")
            .pipe(
                tap(val => console.log(`Calling test method. The response: ${val}`)),
                catchError((error: any) => {
                    debugger
                    console.error(error);
                    return of(error);
                })
            );
    }

  login(user: User) : Observable<any> {
      debugger
      return this.http.post('login', user)
        .pipe(
            map(res => {
              return new LoginStatus('SUCCESS', 'Login Successful');
                debugger
            }),
            catchError((error: any) => {
                console.error(error);
                debugger
                return of(new LoginStatus('FAILURE', 'Username or password is incorrect. Please try again!'));
            }));
  }

}
