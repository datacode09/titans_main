import {SeriesDelegate} from './SeriesDelegate';
import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {Observable, BehaviorSubject} from 'rxjs';
import {RasterParameter} from 'src/core/rasters/RasterParameter';

/**
 * 
 */
export class InternalRasterSeriesDelegate implements SeriesDelegate {
  
  /**
   * 
   */
  public getSeries(rasterId: number, point: any, params: RasterParameter[]): //
    Observable<RasterSeries> {
    const result: BehaviorSubject<RasterSeries> = new BehaviorSubject(null);
    setTimeout(() => {
      const data = [];
      for (let i = 0; i < params.length; i++) {
        data.push({
          x : i
          , y : Math.random()
          , param : params[i]
        });
      }
      const series = new RasterSeries(rasterId, data);
      result.next(series);
    });
    return result;
  }
}