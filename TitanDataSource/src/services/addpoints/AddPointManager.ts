import {Injectable, OnInit} from '@angular/core';
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import {Observable, Subject} from 'rxjs';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {QueryPoint} from 'src/core/rasters/QueryPoint';


@Injectable({
  providedIn: 'root'
  , deps: [RastersService]
})
export class AddPointManager {
  public selectQueryPoint: BehaviorSubject<QueryPoint> = new BehaviorSubject(null);
  private queryPoints: BehaviorSubject<QueryPoint[]> = new BehaviorSubject([]);
  private queryPointActivated: BehaviorSubject<boolean> = new BehaviorSubject(false);
  
  public constructor() {
  }
  
  
  /**
   * 
   */
  public getQueryPoints(): Observable<QueryPoint[]> {
    return this.queryPoints;
  }
  /**
   * 
   */
  public setQueryPoints(arr:QueryPoint[]): void{
    return this.queryPoints.next(arr);
  }
  
  /**
   * 
   */
  public getQueryPointsValue(): QueryPoint[] {
    return this.queryPoints.value;
  }

  /**
   * 
   */
  public getSelectedQueryPoint(): Observable<QueryPoint> {
    return this.selectQueryPoint;
  }
  
  /**
   * 
   */
  public getSelectedQueryPointValue(): QueryPoint {
    return this.selectQueryPoint.value;
  }

  /**
   * 
   */
  public setSelectedQueryPoint(newvalue: QueryPoint): void {
    const currentvalue = this.selectQueryPoint.value;
    if (!(currentvalue === null && newvalue === null)) {
      if (newvalue !== null && currentvalue === null) {
        this.selectQueryPoint.next(newvalue);
      } else if (newvalue === null && currentvalue !== null) {
        this.selectQueryPoint.next(newvalue);
      } else if (newvalue.id !== currentvalue.id) {
        this.selectQueryPoint.next(newvalue);
      }
    }
  }

  
  

  /**
   * 
  */
  public getQueryPointActivated(): Observable<boolean> {
    return this.queryPointActivated;
  }
  
  /**
   * 
  */
  public getQueryPointActivatedValue(): boolean {
    return this.queryPointActivated.getValue();
  }

  /**
   * 
   */
  public setQueryPointActivated(value: boolean): void {
    this.queryPointActivated.next(value);
  }

  /**
   * 
   */
  public toggleQueryPointActivated(): void {
    const currentValue = this.queryPointActivated.getValue();
    this.queryPointActivated.next(!currentValue);
  }

  
}