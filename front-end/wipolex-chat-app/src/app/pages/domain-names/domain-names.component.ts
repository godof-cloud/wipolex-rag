import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component } from '@angular/core';
import { Validators, UntypedFormBuilder, FormControl, ValidationErrors } from '@angular/forms';
import { UploadService } from 'src/app/_services/upload.service';
import { environment } from 'src/environments/environment';
import countries from 'src/assets/json/countries-wipo.json';
import { CountryModel } from 'src/app/_models/country-model';
import { TranslateService } from '@ngx-translate/core';
import { CookieService } from 'ngx-cookie-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-domain-names',
  templateUrl: './domain-names.component.html',
  styleUrls: ['./domain-names.component.scss']
})


export class DomainNamesComponent {
  COMPLAINT_FILE_NAME = 'complaintfile';
  ANNEX_FILE_NAME = 'annexesfile';
  PAY_FILE='paymentfile';
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
  submitted = false;
  displayAuthRepField = false;
  siteKey = environment.recaptchaSiteKey;
  blocked = false;
  isOpen = false;
  displayAuthRepFieldLabel = '';
  fileSize = 0;
  displayMaxTotalSize = 0;
  currentLanguage = this.cookieService.get(this.AMC_LANGUAGE_COOKIE);
  myFormData: any;
  complaintFileList:any;
  annexFileList:any;
  payFileList:any;
  totalFileSizeDialogbox = false;
  paymentLink = '';

  requestForm = this.fb.group({
    complainantFullName: ['', Validators.required],
    complainantAddress: ['', Validators.required],
    complainantCountry: ['', Validators.required],
    complainantEmail: ['', [Validators.required, this.validateEmails]],
    complainantAuthRepType: ['', Validators.required],
    complainantAuthRepFullName: [''],
    complainantAuthRepLawFirm: [''],
    complainantAuthRepCountry: [''],
    complainantAuthRepEmail: ['',[this.validateEmails]],
    caseService: ['', Validators.required],
    respondentFullName: ['', Validators.required],
    respondentEmail: ['', this.validateEmails],
    registrarEmail:['',[this.validateEmails]],
    domainName: ['', Validators.required],
    statusCheckDone: ['', Validators.required],
    domainNameCaseAck: ['', Validators.required],
    complaintfile: [[], {nonNullable: true }],
    annexesfile:  [[], {nonNullable: true }],
    paymentfile: [[], {nonNullable: true }],
    g_recaptcha_response: [null, Validators.required]
  });
 
  

  constructor(private cdr: ChangeDetectorRef,private router: Router,
    private fb: UntypedFormBuilder,
    private uploadService: UploadService, private translate: TranslateService, private cookieService: CookieService) { }

  ngOnInit() {
    
    this.translate.onLangChange.subscribe(() => {
      this.currentLanguage = this.translate.currentLang
    });

    this.paymentLink = environment.paymentLink;
    this.loadCountry();
  }

  loadCountry(){
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
    const complainantAuthRepTypeValue = form.value.complainantAuthRepType;
    console.log('complainantAuthRepTypeValue ---',complainantAuthRepTypeValue)
    if (complainantAuthRepTypeValue === this.DEFAULTAUTHREPTYPE) {
      this.displayAuthRepFieldLabel = '';
      this.displayAuthRepField = false;
      this.requestForm.get('complainantAuthRepCountry').clearValidators();
      this.requestForm.get('complainantAuthRepFullName')?.clearValidators();
      this.requestForm.get('complainantAuthRepLawFirm')?.clearValidators();
      this.requestForm.get('complainantAuthRepEmail')?.clearValidators();
      this.requestForm.get('complainantAuthRepFullName')?.setValue('');
      this.requestForm.get('complainantAuthRepCountry')?.setValue('');
      this.requestForm.get('complainantAuthRepLawFirm')?.setValue('');
      this.requestForm.get('complainantAuthRepEmail')?.setValue('');
    } else {
      this.displayAuthRepFieldLabel = (complainantAuthRepTypeValue === this.INTERNALAUTHREPTYPE ) 
      ? this.translate.instant('filing.complainantAuthRepCompanyName') : this.translate.instant('filing.complainantAuthRepLawFirm');
      this.displayAuthRepField = true;
      
    }
  }


  onFileChange(fname, files : any) {
    console.log('files ----',files)

    if(fname === this.COMPLAINT_FILE_NAME ) { this.complaintFileList = files }
    if(fname === this.ANNEX_FILE_NAME) { this.annexFileList = files }
    if(fname === this.PAY_FILE) { this.payFileList = files }

    console.log('this.complaintFileList---',this.complaintFileList)
    console.log('this.annexFileList---',this.annexFileList)
    console.log('this.payFileList---',this.payFileList)
  }

  get f() { return this.requestForm.controls; }

   getCountryName(key: string, language: string): string | undefined  {
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

  mappedForm(key){
      if(key === 'complainantCountry') {
      const complainantCountryName = this.getCountryName(key, this.currentLanguage);
       this.myFormData.append('complainantCountryName', complainantCountryName);
      } else  if (key === 'complainantAuthRepCountry') {
        const complainantAuthRepCountry = this.getCountryName(key, this.currentLanguage);
        this.myFormData.append('complainantAuthRepCountryName', complainantAuthRepCountry);
      }
      
  }

  checkTotalFileSize(key){
    if( (key === this.COMPLAINT_FILE_NAME || key === this.ANNEX_FILE_NAME || key === this.PAY_FILE) && this.requestForm.value[key] && this.requestForm.value[key].length > 0) {
      console.log(this.requestForm.value[key].length)
      for(let loop=0;loop<this.requestForm.value[key].length;loop++){
        this.fileSize = this.fileSize + this.requestForm.value[key][loop].size;
        console.log(loop +"----"+this.fileSize)
      }
    }

   
  }

  uploadAllFiles() {
    console.log('uploadAllFiles',this.complaintFileList && this.complaintFileList.length > 0)
    if (this.complaintFileList && this.complaintFileList.length > 0) {
      this.complaintFileList.forEach(element => {
            console.log('complaintFileList elements ----',element)
            this.myFormData.append(this.COMPLAINT_FILE_NAME, element);
          });
    } 

    if (this.annexFileList && this.annexFileList.length > 0) {
      this.annexFileList.forEach(element => {
            console.log('annexFileList elements ----',element)
            this.myFormData.append(this.ANNEX_FILE_NAME, element);
          });
      }

      if (this.payFileList && this.payFileList.length > 0) {
        this.payFileList.forEach(element => {
              console.log('payFileList elements ----',element)
              this.myFormData.append(this.PAY_FILE, element);
            });
        }
      
  }

  onSubmit() {
    this.submitted = true;
    this.fileSize = 0;
    console.log('this.requestForm invalid---',this.requestForm.invalid)
    console.log('this.requestForm--->',this.requestForm)
    console.log(this.complaintFileList === undefined || this.complaintFileList.length === 0)
    if(this.requestForm.invalid || (this.complaintFileList === undefined || this.complaintFileList.length === 0)) return;

    this.myFormData = new FormData();
    Object.keys(this.requestForm.controls).forEach((key) => {
      if(this.requestForm.value[key]) {
        if((key !== this.COMPLAINT_FILE_NAME) && (key !== this.ANNEX_FILE_NAME) && (key !== this.PAY_FILE)) { this.myFormData.append(key, this.requestForm.value[key]); }
        this.mappedForm(key)
        this.checkTotalFileSize(key);
      }
    });


    if(this.fileSize > 0) { 
      this.displayMaxTotalSize = Math.round(this.fileSize/Math.pow(1024,2));
      if(this.displayMaxTotalSize > this.TOTAL_FILE_SIZE)  {  this.totalFileSizeDialogbox = true; return; }
    }
   

    this.uploadAllFiles();
    this.myFormData.append('language', this.currentLanguage);


    console.log('myFormData--->',this.myFormData);

    this.uploadService.submitFilingForm(this.myFormData).subscribe(
      response => {
        if (response instanceof HttpResponse) {
          if (response.status === 201) {
            this.complaintFileList = [];
            this.annexFileList = [];
            this.payFileList = [];
            this.blocked = false;
            this.isOpen = true;
            this.submitted = false;
          } else {
            this.blocked = false;
          }
        }
      }, err => {
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
    this.complaintFileList = [];
    this.annexFileList = [];
    this.payFileList = [];
    this.isOpen = false;
    this.submitted = false;
    this.removeEle();
    this.requestForm.reset();

  }

  closeTotalFileSizeDialogbox() {
    this.totalFileSizeDialogbox = false;
  }

 


}
