import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, BehaviorSubject, Subject} from 'rxjs';
import {RastersDelegate} from './delegates/RastersDelegate';

import {HttpRastersDelegate} from './delegates/HttpRastersDelegate';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {RasterEntity} from 'src/core/rasters/RasterEntity';
import {RasterImage} from 'src/core/rasters/RasterImage';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {httpsources} from 'src/conf/conf.json';


@Injectable({
  providedIn: 'root'
  , useFactory: RastersService.singleton
  , deps: [HttpClient]
})
export class RastersService {

  rasterGroups: BehaviorSubject<RastersGroup[]> = new BehaviorSubject<RastersGroup[]>([]);
  rastersmap: Map<number, any> = new Map();
  rasterProperties: Map<number, any> = new Map();
  private selectedItem: BehaviorSubject<any> = new BehaviorSubject(null);
  private rasterParameters: Map<number, BehaviorSubject<RasterParameter[]>> = new Map();
  private selectedRaster: BehaviorSubject<RasterEntity> = new BehaviorSubject(null); 
  
  /**
   * 
   */
  public static singleton(http: HttpClient): RastersService {
    let result: RastersService;
    if (httpsources) {
      result = new RastersService(new HttpRastersDelegate(http));
    } else {
      throw new Error("Not yet supported"); 
    }
    return result;
  }
  
  
  /**
   * 
   */
  public constructor(private rastersDelegate: RastersDelegate) {
    setTimeout(() => {
      rastersDelegate.setHost(this);
      rastersDelegate.loadRasters();
    }, 1000);
    this.getSelectedItem().subscribe(item=>{
      if (item instanceof RasterEntity) {
        this.selectedRaster.next(<RasterEntity> item); 
      }
    }); 
  }
  
  /**
   * 
   */
  public getSelectedItem(): Observable<any> {
    return this.selectedItem;
  }

  /**
   * 
   */
  public getSelectedItemValue(): any {
    return this.selectedItem.value;
  }

  /**
   * 
   */
  public setSelectedItem(item: any): void {
    if (!(item === null && this.selectedItem.value === null)) {
      this.selectedItem.next(item);
    }
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
  public getAllRasters(): Observable<RastersGroup[]> {
    return this.rastersDelegate.getAllRasters();
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
  public getRastersValues(): RastersGroup[] {
    return this.rasterGroups.value;
  }
  
  
  public getSelectedRaster(): Observable<RasterEntity> {
    return this.selectedRaster;
  }



  /**
   * 
   */
  public getRasterGroup(rasterId: number): RastersGroup {
    const arr = this.rasterGroups.getValue();
    const result = arr.find(g => g.rasterIds.indexOf(rasterId) !== -1);
    return result;
  }
}

