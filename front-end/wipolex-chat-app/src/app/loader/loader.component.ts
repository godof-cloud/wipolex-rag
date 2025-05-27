import { Component, OnInit } from '@angular/core';
import { LoaderService } from '../_services/loader.service';
import { TranslateService } from '@ngx-translate/core';
import { ErrorCode } from '../_models/error-code';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css']
})
export class LoaderComponent implements OnInit {
  loading: boolean; message: string; title:string;
  constructor(private loadingService: LoaderService, private translate: TranslateService) {
    this.loadingService.isLoading.subscribe((res) => {
      this.loading = res;
    });

  }

  ngOnInit(): void {
    this.translate.get('errorTitle').subscribe((translation) => {
      this.title = translation;
    });

    this.translate.get('commonErrorMsg').subscribe((translation) => {
      this.message = translation;
    });

    this.loadingService.commonerrorText.subscribe((res) => {
      switch (res) {
        case ErrorCode.InvalidCaptcha:
          this.message = this.translate.instant('invalidCaptchaErrorMsg');
          break;
        default:
          this.message = this.translate.instant('commonErrorMsg');
      }
    });
  }

  closeDialogbox() {
    this.loadingService.hide();
  }

}
