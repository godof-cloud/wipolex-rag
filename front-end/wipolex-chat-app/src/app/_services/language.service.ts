import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { environment } from 'src/environments/environment';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {

  AMC_LANGUAGE_COOKIE = 'amc_language'; // WIPO language standard cookie name
  AMC_LANGUAGE_COOKIE_DOMAIN = environment.cookieDomain;
  supportedLanguages = ['en','ko','es','fr','zh','ar','ru']; // Supported language list
  defaultLanguage = 'en'; // Default language
  currentLanguage;

  constructor(private cookieService: CookieService) { }

  public initialLanguageSetup() {
    // this.translate.addLangs(this.supportedLanguages);
    // this.translate.setDefaultLang(this.defaultLanguage);

    const queryLang = this.getQueryParameterByName(environment.languageQueryParamName);
    const cookieLang = this.cookieService.get(this.AMC_LANGUAGE_COOKIE);

    if (queryLang && this.isLanguageSupported(queryLang)) {
      this.currentLanguage = queryLang;
    } else if (cookieLang && this.isLanguageSupported(cookieLang)) {
      this.currentLanguage = cookieLang;
    } else {
      this.currentLanguage = this.defaultLanguage;
    }
    this.changeLanguage();
  }

  public changeLanguage() {
    // Reset language
    // const languages = this.translate.getLangs();
    // languages.forEach(lang => {
    //   this.translate.resetLang(lang);
    // });

    this.cookieService.set(this.AMC_LANGUAGE_COOKIE, this.currentLanguage, undefined, '/', this.AMC_LANGUAGE_COOKIE_DOMAIN);
    // this.translate.use(this.cookieService.get(this.AMC_LANGUAGE_COOKIE));
  }

  public isLanguageSupported(lang: string): boolean {
    return this.supportedLanguages.indexOf(lang) !== -1;
  }

  getQueryParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
  }
}
