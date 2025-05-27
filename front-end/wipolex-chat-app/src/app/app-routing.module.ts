import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WipoLexComponent } from './pages/wipolex/wipo-lex.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';

// import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: 'en',
    children: [
      { path: '',  component: WipoLexComponent },
      { path: 'page-not-found',component: PageNotFoundComponent }
    ]
  },

  { path: '', redirectTo: 'en', pathMatch: 'full' },
  { path: '**', redirectTo: 'en/page-not-found', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
