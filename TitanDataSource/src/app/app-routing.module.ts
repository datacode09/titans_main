import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ExportApiComponent} from 'src/pages/exportapi/exportapi.component';
import {MainComponent} from 'src/pages/main/main.component';
import {DatasetsComponent} from 'src/pages/datasets/datasets.component';

const routes: Routes = [
  {path: 'main', component: MainComponent}
  , {path: 'export-api', component: ExportApiComponent}
  , {path: 'datasets', component: DatasetsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
//export const routeComponents = routes.map(r => r.component);  
export const routeComponents = [
  MainComponent,
  ExportApiComponent,
  DatasetsComponent
];
