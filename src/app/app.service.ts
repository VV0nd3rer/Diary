import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import {User} from "./user";
import { MessageService } from './message.service';

@Injectable()
export class AppService {

    authenticated = false;

    constructor(private http: HttpClient,
                private messageService: MessageService) {
    }

    authenticate(credentials, callback) {

        const headers = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.username + ':' + credentials.password)
        } : {});

        this.http.get('user', {headers: headers}).subscribe(response => {
            if (response['name']) {
                this.authenticated = true;
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        });

    }
    login(credentials) : Observable<User> {
        return this.http.post<User>('/login', credentials).pipe(
            tap((user: User) => this.log(`logged user with ID w/ id=${user.id}`)),
            catchError(this.handleError<User>('login'))
        );
    }
    private log(message: string) {
        this.messageService.add(`HeroService: ${message}`);
    }
    private handleError<T> (operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            // TODO: send the error to remote logging infrastructure
            console.error(error); // log to console instead

            // TODO: better job of transforming error for user consumption
            this.log(`${operation} failed: ${error.message}`);

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }
}