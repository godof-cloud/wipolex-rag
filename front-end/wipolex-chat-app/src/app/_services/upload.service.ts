import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({ providedIn: 'root' })
export class UploadService {

  constructor(private http: HttpClient) { }


  submitQuestion(question,user): Observable<any> {
    let myFormData = new FormData();
    myFormData.append('question', question);
    const req = new HttpRequest('GET', environment.apiUrl + user +'/inquire?question='+question,  {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

 

 


}
