import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule, routeComponents} from './app-routing.module';
import {AppComponent} from './app.component';
import {ComponentsModule} from 'src/components/components.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {NavComponent} from './nav/nav.component';
import {RouteReuseStrategy} from '@angular/router';
import {CacheRouteReuseStrategy} from './routing/CacheRouteReuseStrategy';
import {FooterComponent} from './footer/footer.component';

@NgModule({
  declarations: [
    AppComponent
    , NavComponent  
    , FooterComponent
    , routeComponents
  ],
  imports: [
    BrowserModule
    , AppRoutingModule
    , ComponentsModule
    , HttpClientModule
  ],
  providers: [
    {
      provide: RouteReuseStrategy,
      useClass: CacheRouteReuseStrategy
    }
    , {
      provide: RastersService
      , useFactory: RastersService.singleton
      , deps: [HttpClient]
    }
  ],

  bootstrap: [AppComponent]
})
export class AppModule {}
