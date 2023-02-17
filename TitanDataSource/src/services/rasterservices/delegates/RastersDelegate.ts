import {Observable} from 'rxjs';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {RasterImage} from 'src/core/rasters/RasterImage';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {Clazz} from 'src/core/rasters/Clazz';


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
  getParameters(rasterId: number, clazzes:Map<string, Clazz>): Observable<RasterParameter[]>; 
  
   
  /**
   * 
   */
  getAllRasters(): Observable<RastersGroup[]>; 
}
