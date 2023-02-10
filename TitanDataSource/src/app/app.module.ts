import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule, routeComponents} from './app-routing.module';
import {AppComponent} from './app.component';
import {ComponentsModule} from 'src/components/components.module';
import {HttpClient, HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {NavComponent} from './nav/nav.component';
import {RouteReuseStrategy} from '@angular/router';
import {CacheRouteReuseStrategy} from './routing/CacheRouteReuseStrategy';
import {FooterComponent} from './footer/footer.component';
import {HeaderComponent} from './header/header.component';
import {FormsModule} from '@angular/forms';
import {AuthInterceptor} from './auth/AuthInterceptor';

@NgModule({
  declarations: [
    AppComponent
    , NavComponent
    , FooterComponent
    , HeaderComponent
    , routeComponents
  ],
  imports: [
    BrowserModule
    , FormsModule
    , AppRoutingModule
    , ComponentsModule
    , HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor, multi: true
    },
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
