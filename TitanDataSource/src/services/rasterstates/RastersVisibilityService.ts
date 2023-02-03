import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {RastersService} from '../rasterservices/RastersService';

/**
 * 
 */
@Injectable({
  providedIn: 'root'
  , deps : [RastersService]
})
export class RastersVisibilityService {

  private map: Map<RasterParameter, BehaviorSubject<boolean>> = new Map();

  /**
   * 
   */
  public constructor(private service:RastersService) {
  }

  /**
   * 
   */
  public getVisibility(rasterParam: RasterParameter): BehaviorSubject<boolean> {
    if (!this.map.has(rasterParam)) {
      const instance: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
      instance.subscribe(s => {
        if (s) {
          this.map.forEach(v => {
            if (v != instance) {
              v.next(false);
            }
          });
          this.service.setSelectedItem(rasterParam); 
        }
      });
      this.map.set(rasterParam, instance);
    }
    const result = this.map.get(rasterParam);
    return result;
  }

}

