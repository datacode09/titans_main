import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http/';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
  , deps: [HttpClient, Router]
})
export class TimeSeriesServices {

}

