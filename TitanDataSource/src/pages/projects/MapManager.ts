import {Inject, Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MapManager {

  private activateDraw: BehaviorSubject<boolean> = new BehaviorSubject(false);

  private map: BehaviorSubject<any> = new BehaviorSubject(null);

  /**
   * 
   */
  public setMap(map: any): void {
    this.map.next(map);
  }

  /**
   * 
   */
  public getMap(): Subject<any> {
    return this.map;
  }

  /**
   * 
   */
  public setActivateDrawGeography(value: boolean): void {
    this.activateDraw.next(value);
  }

  /**
   * 
   */
  public getActivateDrawGeography(): Subject<boolean> {
    return this.activateDraw;
  }

  /**
   * 
   */
  public getActivateDrawGeographyValue(): boolean {
    return this.activateDraw.value;
  }
}