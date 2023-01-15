import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http/http';
import {Router} from '@angular/router';
import {BehaviorSubject, Subject} from 'rxjs';
import {RasterParameter} from './RasterParameter';

/**
 * 
 */
@Injectable({
  providedIn: 'root'
  , deps: [HttpClient, Router]
})
export class RastersVisibilityService {

  private map: Map<RasterParameter, BehaviorSubject<boolean>> = new Map();

  /**
   * 
   */
  public constructor() {
  }

  /**
   * 
   */
  public getVisibility(rasterParam: RasterParameter): BehaviorSubject<boolean> {
    if (!this.map.has(rasterParam)) {
      const instance: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
      this.map.set(rasterParam, instance);
    }
    const result = this.map.get(rasterParam); 
    return result;
  }
  
}

