import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DomainNamesComponent } from './pages/domain-names/domain-names.component';
import { DomainsComponent } from './pages/domains/domains.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { DomainNamesResponseComponent } from './pages/domain-names-response/domain-names-response.component';

// import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [

  {
    path: 'en',
    children: [
      { path: '',  component: DomainsComponent },
      { path: 'filing/response', component: DomainNamesResponseComponent },
      { path: 'filing/complaint', component: DomainNamesComponent },
      { path: 'filing', component: DomainNamesComponent },
      { path: 'page-not-found',component: PageNotFoundComponent }
    ]
  },

  { path: '', redirectTo: 'en', pathMatch: 'full' },
  { path: 'filing/response', redirectTo: 'en/filing/response', pathMatch: 'full' },
  { path: 'filing/complaint', redirectTo: 'en/filing/complaint', pathMatch: 'full' },
  { path: 'filing', redirectTo: 'en/filing', pathMatch: 'full' },
  { path: '**', redirectTo: 'en/page-not-found', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
