// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  contextIsRoot: false,
  paymentLink:'https://www3dev.wipo.int/amc-payment',
  webComponentsPath: '',
  apiUrl: 'https://wwwdev.wipo.int/domains-api/api/v1/',
  recaptchaSiteKey: '',
  cookieDomain: 'localhost',
  languageQueryParamName: 'lang',
  siteUrl:'https://wwwdev.wipo.int/',
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
