import {Inject, Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';

@Injectable({
  providedIn : 'root'
})
export class MapManager {
  
  private map: BehaviorSubject<any> = new BehaviorSubject(null);
  
  /**
   * 
   */
  public setMap(map:any):void {
    this.map.next(map);  
  }
  
  /**
   * 
   */
  public getMap(): Subject<any> {
    return this.map;
  }
}