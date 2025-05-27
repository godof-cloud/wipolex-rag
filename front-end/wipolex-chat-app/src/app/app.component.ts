import { Component, HostListener, OnInit, Renderer2, RendererFactory2, Inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from './_services/language.service';
import { CookieService } from 'ngx-cookie-service';
import { environment } from '../environments/environment';
import { Router, Event, NavigationEnd, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  AMC_LANGUAGE_COOKIE = 'amc_language'; // WIPO language standard cookie name
  AMC_FORMS_APP_ID = 'app-0038'
  languageList = '[{ "code": "en"}, {"code": "es"}]';
  navbarTitle = '';
  appLink = '';
  pageKey = '';
  appCategory = '';
  contactUsOptions = new Array<any>();
  sticky = false;
  private renderer: Renderer2;
  pageData: any;
  languages: any[];
  langSelected = false;
  permission = true;
  linkrokMCST = '';
  linkMediation = '';
  linkUnilateral = '';
  linkgoodOffices = '';
  langue = '';
  officeDetails = [];

  currentRoute: string;
  officesParam: string = '';
  officeName = '';
  navbarAppname: string = '';
  filingLink = '/filing';
  complaintLink = '/filing/complaint';
  responseLink = '/filing/response';
  tabfilingLink = '';
  tabcomplaintLink = '';
  tabresponseLink = '';
  domainsBreadcrumb = [];
  DomainMainItemName = "";

  @HostListener('window:scroll', []) onWindowScroll() {
    const verticalOffset = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
    this.sticky = verticalOffset > 50;
  }

  constructor(private translate: TranslateService, public languageService: LanguageService, private activeRoute: ActivatedRoute,
    private rendererFactory: RendererFactory2, private cookieService: CookieService, private router: Router) {

    this.currentRoute = "";
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.url;
        this.setPageKey(this.currentRoute);
        this.setAppLink()
      }
    });

    this.renderer = this.rendererFactory.createRenderer(null, null);


  }
  ngOnInit() {

    this.langue = '';
    document.addEventListener("wipoLanguageChange", (e: any) => {
      this.langue = e.detail.languageSelected;
      this.updateWipoLanguageCookie(this.langue);
      this.languageService.currentLanguage = this.langue;
      this.languageService.changeLanguage();
    });

    this.languageService.currentLanguage = 'en';
   

    this.languageService.changeLanguage();

    this.updateWipoLanguageCookie(this.languageService.currentLanguage);

    this.appLink = environment.siteUrl + this.languageService.currentLanguage;

    // set initial language
    this.languageService.initialLanguageSetup();

    this.tabfilingLink = this.languageService.currentLanguage + this.filingLink;
    this.tabcomplaintLink = this.languageService.currentLanguage + this.complaintLink;
    this.tabresponseLink =  this.languageService.currentLanguage +this.responseLink;

    this.setBreadcrumb();

  }

  setBreadcrumb() {
    this.domainsBreadcrumb = [];
    this.translate.get('domains.breadcrumbTitle').subscribe((translated: string) => {
        this.DomainMainItemName = translated;
        this.domainsBreadcrumb = [{ itemName: this.DomainMainItemName, itemURL: "" } ];
      if(this.isFiling()) {
        this.domainsBreadcrumb.push({ itemName: this.translate.instant('domains.SecondTitle'), itemURL: this.tabfilingLink });
      } else {
        this.domainsBreadcrumb.push({ itemName: this.translate.instant('domains.homeSecondTitle'), itemURL: this.languageService.currentLanguage  });
      }
    });
   
  }


  isFiling() {
    if (window.location.href.includes('/filing')) {
       return true;
    }
    return false;
  }

  private updateWipoLanguageCookie(language: string) {
    this.cookieService.delete(this.AMC_LANGUAGE_COOKIE);
    this.cookieService.set(this.AMC_LANGUAGE_COOKIE, language, 0, '/');
  }

  private _initLanguages() {
    this.languages = [{ "code": "en", "name": this.translate.instant('language.selector.en') }]
  }

  setAppLink() {
    this.appLink = window.location.origin + '/domains/';
  }

  setPageKey(url) {
    if (url.includes('/complaint') || url.includes('/filing')) {
       this.pageKey = 'complaint';
    } else if (url.includes('/response')) {
      this.pageKey = 'complaint';
    } else {
        this.pageKey = 'model-forms'; 
    }
  }


  setContactUsOptions() {
    this.contactUsOptions.push({
      code: 'contact',
      link: 'https://www3.wipo.int/contact/en/area.jsp?area=arbitration'
    });
    this.contactUsOptions.push({
      code: 'termsOfUse',
      link: 'https://www.wipo.int/tools/en/disclaim-ipportal.html'
    });
    this.contactUsOptions.push({
      code: 'privacyPolicy',
      link: 'https://www.wipo.int/tools/en/privacy_policy-ipportal.html'
    });

  }

  setApplicationCategory() {
    this.appCategory = 'domain';
  }



}