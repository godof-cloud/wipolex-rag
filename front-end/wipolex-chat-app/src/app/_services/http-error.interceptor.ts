import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { LoaderService } from '../_services/loader.service';

export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private LoaderService: LoaderService) { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        retry(1),
        catchError((error: HttpErrorResponse) => {
          let errorMessage = '';
          if (error.error instanceof ErrorEvent) {
            // client-side error
            errorMessage = `Message: ${error.error.message}`;
          } else {
            // server-side error
            errorMessage = `Error Code: ${error.error.errorCode}\nMessage: ${error.error.message}`;
            let errorResponse = JSON.parse(error.error);
            this.LoaderService.load(errorResponse.errorCode);
          }
          
          this.LoaderService.show();
          return throwError(errorMessage);
        })
      )
  }
}