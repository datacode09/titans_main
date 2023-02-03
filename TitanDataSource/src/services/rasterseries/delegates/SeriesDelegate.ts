import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {Observable} from 'rxjs';
import {RasterParameter} from 'src/core/rasters/RasterParameter';

/**
 * 
 */
export interface SeriesDelegate {
  
  /**
   * 
   */
  getSeries(rasterId: number, point: any, params: RasterParameter[]): Observable<RasterSeries>;
}