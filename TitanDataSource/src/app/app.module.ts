import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ComponentsModule} from 'src/components/components.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {RastersService} from 'src/services/rasterservices/RastersService';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
    , ComponentsModule
    , HttpClientModule 
  ],
  providers: [
    {
      provide: RastersService
      , useFactory: RastersService.singleton
      , deps : [HttpClient]
    }
  ],
  
  bootstrap: [AppComponent]
})
export class AppModule { }
