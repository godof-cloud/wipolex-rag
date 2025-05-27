import { Observable, Subject, from, of } from 'rxjs';
import {
  mergeMap,
  take
} from 'rxjs/operators';

import { environment } from '../../environments/environment';

const wPath = environment.webComponentsPath;

function loadScript(
  src: string,
  async = false,
): Observable<void> {
  const subject = new Subject<void>();
  document.head.appendChild(
    Object.assign(document.createElement('script'), {
      src,
      async,
      onload: () => {
        subject.next();
      },
      onerror: (error: any) => {
        console.error('load failure', src, error);
        subject.next();
      }
    })
  );
  return subject.pipe(take(1));
}

export const loadNavbar: () => Promise<void> = () => {
    // first load polyfills
    return loadScript(`${wPath}polyfills/webcomponents-loader.js`)
      // then load remaining async
      .pipe(
        mergeMap(() =>
          from([
            `${wPath}wipo-navbar/wipo-navbar.js`,
            `${wPath}wipo-init/wipo-init.js`
          ]).pipe(
            mergeMap(url => loadScript(url, true)),
          )
        )
      )
      .toPromise();
};