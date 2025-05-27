import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component } from '@angular/core';
import { Validators, UntypedFormBuilder, FormControl, ValidationErrors } from '@angular/forms';
import { UploadService } from 'src/app/_services/upload.service';
import { environment } from 'src/environments/environment';
import countries from 'src/assets/json/countries-wipo.json';
import { CountryModel } from 'src/app/_models/country-model';
import { TranslateService } from '@ngx-translate/core';
import { CookieService } from 'ngx-cookie-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-domain-names-response',
  templateUrl: './domain-names-response.component.html',
  styleUrls: ['./domain-names-response.component.scss']
})


export class DomainNamesResponseComponent {
  RESPONSE_FILE_NAME = 'responsefileAttachment';
  ANNEX_FILE_NAME = 'annexesfileAttachment';
  DEFAULTAUTHREPTYPE = 'SELF';
  INTERNALAUTHREPTYPE = 'INTERNAL';
  EXTERNALAUTHREPTYPE = 'EXTERNAL';
  TOTAL_FILE_SIZE = 50;
  AMC_LANGUAGE_COOKIE = 'amc_language'; // WIPO language standard cookie name
  filename: string;
  currentFile: File;
  public countryList = countries;
  countryOptions: any[] = [];
  temp: any[] = [];
  currentLanguage = this.cookieService.get(this.AMC_LANGUAGE_COOKIE);
  managerInitials = '';
  displayMaxTotalSize = 0;
  fileSize = 0;
  submitted = false;
  displayAuthRepField = false;
  siteKey = environment.recaptchaSiteKey;
  blocked = false;
  isOpen = false;
  IsComplaintfiled = false;
  displayAuthRepFieldLabel = '';
  myFormData: any;
  responseFileList:any;
  annexFileList:any;
  totalFileSizeDialogbox = false;

  requestForm = this.fb.group({
    caseNumber:['', Validators.required],
    respondentFullName: ['', Validators.required],
    respondentEmail: ['', [Validators.required, this.validateEmails]],
    respondentCountry:['', Validators.required],
    respondentAuthRepType: ['', Validators.required],
    respondentAuthRepFullName: [''],
    respondentAuthRepLawFirm: [''],
    respondentAuthRepCountry: [''],
    respondentAuthRepEmail: ['',[this.validateEmails]],
    domainName: ['', Validators.required],
    complainantRepresentativeEmail: ['',[this.validateEmails]],
    response:['', Validators.required],
    responsefileAttachment: [[], {nonNullable: true }],
    annexesfileAttachment: [[], {nonNullable: true }],
    g_recaptcha_response: [null, Validators.required]
  });
 

  constructor(private cdr: ChangeDetectorRef,
    private fb: UntypedFormBuilder,private router: Router,
    private uploadService: UploadService, private translate: TranslateService, 
    private cookieService: CookieService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.managerInitials  = this.route.snapshot.queryParams['managerInitials'];
    this.translate.onLangChange.subscribe(() => {
      this.currentLanguage = this.translate.currentLang

    });

   this.loadCountry();
  }

  loadCountry() {
    this.countryOptions = [];
    let country: CountryModel;
    this.countryList.forEach(element => {
      country = new CountryModel();
      if (this.currentLanguage === 'es') {
        country.label = element["Spanish - Display Name"];
        country.value = element["WIPO_CODE"];
      } else if (this.currentLanguage === 'fr') {
        country.label = element["French - Display Name"];
        country.value = element["WIPO_CODE"];
      } else {
        country.label = element["Country Name"];
        country.value = element["WIPO_CODE"];
      }
      this.countryOptions.push(country);

    });
    this.countryOptions.sort((a: any, b: any) => a.label > b.label ? 1 : (a.label < b.label ? -1 : 0));
  }


  validateEmails(emails: FormControl): ValidationErrors | null {
    if (emails.value) {
      // Extract the email string from the form control
      const emailString = emails.value;

      // Split the email string into individual email addresses
      const emailList = emailString.split(';').map(email => email.trim());

      // Validate each email address using a regex
      const EMAIL_REGEXP = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

      // Check if any email address is invalid
      if (emailList.some(email => !EMAIL_REGEXP.test(email))) {
        return { invalidEmails: true };
      }
      return null;
    }
  }

  selectAuthRepType(form) {
    const respondentAuthRepTypeValue = form.value.respondentAuthRepType;
    console.log('respondentAuthRepTypeValue---',respondentAuthRepTypeValue)
    if (respondentAuthRepTypeValue === this.DEFAULTAUTHREPTYPE) {
      this.displayAuthRepFieldLabel = '';
      this.displayAuthRepField = false;
      this.requestForm.get('respondentAuthRepFullName').clearValidators();
      this.requestForm.get('respondentAuthRepCountry').clearValidators();
      this.requestForm.get('respondentAuthRepLawFirm').clearValidators();
      this.requestForm.get('respondentAuthRepEmail').clearValidators();
      this.requestForm.get('respondentAuthRepFullName')?.setValue('');
      this.requestForm.get('respondentAuthRepCountry')?.setValue('');
      this.requestForm.get('respondentAuthRepLawFirm')?.setValue('');
      this.requestForm.get('respondentAuthRepEmail')?.setValue('');
    } else {
      this.displayAuthRepFieldLabel = (respondentAuthRepTypeValue === this.INTERNALAUTHREPTYPE ) 
      ? this.translate.instant('response.respondentAuthRepCompanyName') : this.translate.instant('response.respondentAuthRepLawFirm');
      this.displayAuthRepField = true;
    }
  }
 

  onFileChange(fname, files) {
    if(fname === this.RESPONSE_FILE_NAME ) { this.responseFileList = files }
    if(fname === this.ANNEX_FILE_NAME) { this.annexFileList = files }
  }




  get f() { return this.requestForm.controls; }

  getCountryName(key: string, language: string): string | undefined {
    const country = this.countryList.find(
      (element) => element["WIPO_CODE"] === this.requestForm.value[key]
    );
    if (!country) {
      return undefined;
    }
    switch (language) {
      case 'es':
        return country["Spanish - Display Name"];
      case 'fr':
        return country["French - Display Name"];
      default:
        return country["Country Name"];
    }
  };

  mappedForm(key) {
    if (key === 'respondentCountry') {
      const respondentCountryName  = this.getCountryName(key, this.currentLanguage);
      this.myFormData.append('respondentCountryName ', respondentCountryName );
    } else if (key === 'respondentAuthRepCountry') {
      const respondentAuthRepCountryName  = this.getCountryName(key, this.currentLanguage);
      this.myFormData.append('respondentAuthRepCountryName ', respondentAuthRepCountryName );
    }
   
  }

  checkTotalFileSize(key){
    if( (key === this.RESPONSE_FILE_NAME || key === this.ANNEX_FILE_NAME) && this.requestForm.value[key] && this.requestForm.value[key].length > 0) {
      console.log(this.requestForm.value[key].length)
      for(let loop=0;loop<this.requestForm.value[key].length;loop++){
        this.fileSize = this.fileSize + this.requestForm.value[key][loop].size;
        console.log(loop +"----"+this.fileSize)
      }

     
    }
  }

  uploadAllFiles() {
    if (this.responseFileList && this.responseFileList.length > 0) {
      this.responseFileList.forEach(element => {
            console.log('responseFileList elements ----',element)
            this.myFormData.append(this.RESPONSE_FILE_NAME, element);
          });
      }

    if (this.annexFileList && this.annexFileList.length > 0) {
      this.annexFileList.forEach(element => {
            console.log('annexFileList elements ----',element)
            this.myFormData.append(this.ANNEX_FILE_NAME, element);
          });
      }
  }


  onSubmit() {
    this.fileSize = 0;
    this.myFormData = new FormData();
    this.submitted = true;
    if (this.requestForm.invalid)  return;
    console.log('this.requestForm---',this.requestForm)
    
    Object.keys(this.requestForm.controls).forEach((key) => {
      if(this.requestForm.value[key]) {
        if((key !== this.RESPONSE_FILE_NAME) && (key  !== this.ANNEX_FILE_NAME)) { this.myFormData.append(key, this.requestForm.value[key]); }
        this.mappedForm(key)
        this.checkTotalFileSize(key);
      }
    });

    const mIVal =  (this.managerInitials) ?  this.managerInitials : null;
    this.myFormData.append('managerInitials', mIVal)
    this.myFormData.append('language', this.currentLanguage);


    if(this.fileSize > 0) {
      this.displayMaxTotalSize = Math.round(this.fileSize/Math.pow(1024,2));
      if(this.displayMaxTotalSize > this.TOTAL_FILE_SIZE)  { this.totalFileSizeDialogbox = true; return; }  
    }

    
    this.uploadAllFiles();

    this.uploadService.submitResponseFilingForm(this.myFormData).subscribe(
      response => {
        if (response instanceof HttpResponse) {
          if (response.status === 201) {
            this.blocked = false;
            this.isOpen = true;
            this.responseFileList = [];
            this.annexFileList = [];
          } else {
            this.blocked = false;
          }
        }
      }, err => {
        //  alert("Error caught at Subscriber " + err)
        this.blocked = false;
      }, () => { this.blocked = false; });
  }


  removeEle() {
    let elements = document.getElementsByClassName('b-input__multi-select--badge ng-star-inserted');
    while (elements.length > 0) {
      elements[0].parentNode.removeChild(elements[0]);
    }
  }

  closeDialogbox() {
    this.isOpen = false;
    this.submitted = false;
    this.removeEle();
    this.requestForm.reset();
  }

  closeTotalFileSizeDialogbox() {
    this.totalFileSizeDialogbox = false;
  }


}
