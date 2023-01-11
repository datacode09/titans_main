import {Observable} from 'rxjs';
import {RasterImage} from '../RasterImage';

/**
 * 
 */
export interface RastersDelegate {
  /**
   * 
   */
  setHost(host: any): void;
  /**
   * 
   */
  loadRasters(): void;

  /**
   * 
   */
  loadRasterEntity(rasterId: number, callback: any): void
  
  
  getRasterImage(rasterId: number): Observable<RasterImage>;
}
