import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {jqxTreeModule} from 'jqwidgets-ng/jqxtree/';  
import { jqxExpanderModule } from 'jqwidgets-ng/jqxexpander';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {ComponentsModule} from 'src/components/components.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
    , ComponentsModule
    , jqxTreeModule 
    , jqxExpanderModule 
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
