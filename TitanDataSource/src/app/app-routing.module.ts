import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ExportApiComponent} from 'src/pages/exportapi/exportapi.component';
import {MainComponent} from 'src/pages/main/main.component';
import {DatasetsComponent} from 'src/pages/datasets/datasets.component';
import {ModelsComponent} from 'src/pages/models/models.component';
import {HomeComponent} from 'src/pages/home/home.component';
import {LoginComponent} from 'src/pages/login/login.component';

const routes: Routes = [
  {
    path: 'login', component: LoginComponent,
  }
  , {
    path: '', component: LoginComponent,
  }
  
  , {
    path: 'home', component: HomeComponent, children: [
      {path: 'main', component: MainComponent}
      , {path: 'export-api', component: ExportApiComponent}
      , {path: 'datasets', component: DatasetsComponent}
      , {path: 'models', component: ModelsComponent}
      , {path: '', component: MainComponent}
    ]
  }
  , {
    path: '**', component: LoginComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
export const routeComponents = [
  MainComponent,
  ExportApiComponent,
  DatasetsComponent,
  ModelsComponent,
  HomeComponent,
  LoginComponent
];
