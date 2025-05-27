import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA } from '@angular/core';

import { APP_BASE_HREF , DatePipe, registerLocaleData } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { HttpClientModule,HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { environment } from 'src/environments/environment';
import { loadNavbar } from './_shared/loader';
import { HttpErrorInterceptor } from './_services/http-error.interceptor';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WInputRadioModule ,WDialogModule, WFilterModule,WBlockUiModule,WInputCheckboxOneModule, FFacetModule, WActionBarModule, WArticleModule, WBoxModule, WButtonGroupModule, WButtonModule, WEditPanelModule, WFieldModule, WInputTextareaModule, WInputTextModule, WPageHeaderModule, WPageSectionModule, WSectionModule, WSelectManyModule, WSelectOneModule, WSlotModule, WStepModule, WStickyBarModule, WValidationPanelModule, WViewModule, WViewPanelModule } from '@wipo/w-angular';
import{ WInputDateModule, WInputFileModule, registerLocale } from '@wipo/w-angular/primeng';

import { RecaptchaFormsModule, RecaptchaModule } from "ng-recaptcha";

import {TableModule} from 'primeng/table';
import {PaginatorModule} from 'primeng/paginator';
import { LoaderComponent } from './loader/loader.component';
import { OAuthModule } from 'angular-oauth2-oidc';
import { LoaderService } from './_services/loader.service';

import localeEs from '@angular/common/locales/es';

import { DomainNamesComponent } from './pages/domain-names/domain-names.component';
import { DomainsComponent } from './pages/domains/domains.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { DomainNamesResponseComponent } from './pages/domain-names-response/domain-names-response.component';

registerLocale(localeEs);


@NgModule({
  declarations: [
    AppComponent,
    DomainNamesComponent,
    DomainsComponent,
    LoaderComponent,
    PageNotFoundComponent,
    DomainNamesResponseComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    TableModule,
    FFacetModule,
    WActionBarModule,
    WArticleModule,
    WBoxModule,
    WButtonGroupModule,
    WButtonModule,
    WEditPanelModule,
    WFieldModule,
    WInputTextModule,
    WInputTextareaModule,
    WPageHeaderModule,
    WPageSectionModule,
    WSectionModule,
    WSelectManyModule,
    WSelectOneModule,
    WSlotModule,
    WStepModule,
    WStickyBarModule,
    WViewModule,
    WViewPanelModule,
    WInputFileModule,
    WInputDateModule,
    RecaptchaModule,
    RecaptchaFormsModule,
    WValidationPanelModule,
    WInputRadioModule,
    WDialogModule,
    PaginatorModule,
    WBlockUiModule,
    WInputCheckboxOneModule,WFilterModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
  providers: [
   {
     provide: APP_BASE_HREF,
     useValue: environment.contextIsRoot ? '/' : '/' + (window.location.pathname.split('/')[1] || '')
   },    
   
    {
      provide: APP_INITIALIZER,
      useFactory: () => loadNavbar,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      deps: [LoaderService],
      multi: true
    },
    DatePipe
  ],
  
  bootstrap: [AppComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}
