import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({ providedIn: 'root' })
export class UploadService {

  constructor(private http: HttpClient) { }


  submitFilingForm(formdata: FormData): Observable<HttpEvent<{}>> {
    const req = new HttpRequest('POST', environment.apiUrl + 'efiling', formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);

  }

  submitResponseFilingForm(formdata: FormData): Observable<HttpEvent<{}>> {
    const req = new HttpRequest('POST', environment.apiUrl + 'domain-name-response/efiling', formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);

  }

 


}
