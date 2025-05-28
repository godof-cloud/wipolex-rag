import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { catchError, map } from 'rxjs/operators';


@Injectable({ providedIn: 'root' })
export class UploadService {

  constructor(private http: HttpClient) { }


  submitQuestion1(question, user): Observable<any> {
    let params = new HttpParams().set('question', question);
    const headers = new HttpHeaders({});

    return this.http.get(environment.apiUrl + user + '/inquire', {
      headers: headers,
      params: params,
      responseType: 'text' // <-- This is the crucial part: expect text
    }).pipe(
      map((responseText: string) => {
        // Now, parse the text response into a JSON object
        try {
          const jsonResponse = JSON.parse(responseText);
          console.log('Successfully converted text to JSON:', jsonResponse);
          return jsonResponse;
        } catch (e) {
          console.error('Failed to parse JSON from text response:', e);
          // If JSON parsing fails, throw an error or return a specific error structure
          throw new Error('Invalid JSON format received from API.');
        }
      }),
      catchError(error => {
        // Handle HTTP errors (e.g., network issues, 4xx, 5xx status codes)
        console.error('API call error:', error);
        // You can extract a more user-friendly message from the error object
        return throwError(() => new Error('Failed to get AI response. Please try again later.'));
      })
    );


  }

  submitQuestion(question, user): Observable<any> {
    let myFormData = new FormData();
    myFormData.append('question', question);
    const req = new HttpRequest('GET', environment.apiUrl + user + '/inquire?question=' + question, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);

  }






}
