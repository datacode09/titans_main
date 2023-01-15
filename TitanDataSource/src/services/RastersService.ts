import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, BehaviorSubject, Subject } from 'rxjs';
import {map} from 'rxjs/operators/';
import {RastersGroup} from './RastersGroup';
import {RasterEntity} from './RasterEntity';
import {RastersDelegate} from './delegates/RastersDelegate';
import {InternalRastersDelegate} from './delegates/InternalRastersDelegate';
import {HttpRastersDelegate} from './delegates/HttpRastersDelegate';
import {RasterImage} from './RasterImage';
import {RasterParameter} from './RasterParameter';

@Injectable({
  providedIn: 'root'
  , useFactory: RastersService.singleton
  , deps: [HttpClient]
})
export class RastersService {

  rasterGroups: BehaviorSubject<RastersGroup[]> = new BehaviorSubject<RastersGroup[]>([]);
  rastersmap: Map<number, any> = new Map();
  rasterProperties: Map<number, any> = new Map();

  /**
   * 
   */
  public static singleton(http: HttpClient): RastersService {
        return new RastersService(new InternalRastersDelegate());
//    return new RastersService(new HttpRastersDelegate(http));
  }

  /**
   * 
   */
  public constructor(private rastersDelegate: RastersDelegate) {
    setTimeout(() => {
      rastersDelegate.setHost(this);
      rastersDelegate.loadRasters();
    }, 1000);
  }

  /**
   * 
   */
  public getRasterEntity(rasterId: number): Subject<RasterEntity> {
    const s: BehaviorSubject<RasterEntity> = new BehaviorSubject(null);
    if (this.rastersmap.has(rasterId)) {
      s.next(this.rastersmap.get(rasterId));
    } else {
      this.rastersDelegate.loadRasterEntity(rasterId, (e: RasterEntity) => {
        s.next(e);
      })
    }
    return s;
  }

  /**
   * 
   */
  public getMapImage(param: RasterParameter): Observable<RasterImage> {
    return this.rastersDelegate.getRasterImage(param);
  }

  /**
   * 
   */
  public getRasters(): Observable<RastersGroup[]> {
    return this.rasterGroups;
  }
  
    
  /**
   * 
   */
  public getParameters(rasterId: number): Observable<RasterParameter[]> {
    const result:Observable<RasterParameter[]> 
    = this.rastersDelegate.getParameters(rasterId);
    return result;
  } 
}

