import {Observable} from 'rxjs';
import {RasterImage} from '../RasterImage';
import {RasterParameter} from '../RasterParameter';

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
  
  
  /**
   * 
   */
  getRasterImage(param: RasterParameter): Observable<RasterImage>;
  
  /**
   * 
   */
  getParameters(rasterId: number): Observable<RasterParameter[]>; 
}
