import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {Observable} from 'rxjs';

/**
 * 
 */
export interface SeriesDelegate {
  
  /**
   * 
   */
  getSeries(rasterId: number): Observable<RasterSeries>;
}