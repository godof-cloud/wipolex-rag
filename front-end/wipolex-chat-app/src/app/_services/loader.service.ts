import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {
  public commonerrorText = new BehaviorSubject(0);
  public isLoading = new BehaviorSubject(false);
  constructor() { }

  show() {
    this.isLoading.next(true);
  }

  load(errorCode: any){
    this.commonerrorText.next(errorCode);
  }

  hide() {
    this.isLoading.next(false);
  }
}
