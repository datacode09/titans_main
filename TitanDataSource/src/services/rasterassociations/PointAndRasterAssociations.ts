
import {Injectable} from '@angular/core';
import {AddPointManager} from '../addpoints/AddPointManager';
import {Observable, BehaviorSubject} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class PointAndRasterAssociations {

  private associations: Map<number, BehaviorSubject<number[]>> = new Map();

  /**
   * 
   */
  public constructor(private manager: AddPointManager) {
    this.syncWithQueryPoints();
  }

  /**
   * 
   */
  public getAssociations(pointId: number): Observable<number[]> {
    this.initAssociations(pointId);
    const result = this.associations.get(pointId);
    return result;
  }
  
  /**
   * 
   */
  public hasAssociation(queryId: number, rasterId: number): boolean {
    const arr = this.getAssociationsValue(queryId);
    const index = arr.findIndex(d => d === rasterId);
    const result = index !== -1;
    return result;
  } 

  /**
   * 
   */
  private syncWithQueryPoints(): void {
    this.manager.getQueryPoints().subscribe(arr => {
      const keys = this.associations.keys();
      let n = keys.next();
      const toremove: number[] = [];
      while (!n.done) {
        const index = arr.findIndex(e => e.id === n.value);
        if (index === -1) {
          toremove.push(n.value);
        }
        n = keys.next()
      }
      toremove.forEach(i => this.associations.delete(i));
    });
  }

  /**
   * 
   */
  public save(queryPointId: number, rasterIds: number[]) {
    const querypointassociations = this.associations.get(queryPointId);
    querypointassociations.next(rasterIds); 
  }

  /**
   * 
   */
  public getAssociationsValue(queryPointId: number): number[] {
    this.initAssociations(queryPointId);
    const result = this.associations.get(queryPointId).value;
    return result;
  }

  private initAssociations(queryPointId:number): void {
    if (!this.associations.has(queryPointId)) {
      this.associations.set(queryPointId, new BehaviorSubject([]));
    }
  }

}
