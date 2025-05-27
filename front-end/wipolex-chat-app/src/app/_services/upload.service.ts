import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({ providedIn: 'root' })
export class UploadService {

  constructor(private http: HttpClient) { }


  submitQuestion(question): Observable<HttpEvent<{}>> {
    let myFormData = new FormData();
    myFormData.append('question', question);
    const req = new HttpRequest('POST', environment.apiUrl + 'searchingAnswer', myFormData, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

 

 


}
