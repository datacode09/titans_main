import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {Observable} from 'rxjs';
import {Clazz} from 'src/core/rasters/Clazz';


export interface ParamDelegate {
  /**
   * 
   */
  loadRasterParameters(rasterId:number, clazzes:Map<string, Clazz>):Observable<RasterParameter[]>
  
  
  /**
   * 
   */
  loadRasterClasses(rasterId:number):Observable<Map<string, Clazz[]>>
}