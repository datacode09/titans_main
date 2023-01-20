import {Injectable} from '@angular/core';
import {Observable, BehaviorSubject} from 'rxjs';
import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {HttpClient} from '@angular/common/http';
import {InternalRasterSeriesDelegate} from './delegates/InternalRasterSeriesDelegate';
import {SeriesDelegate} from './delegates/SeriesDelegate';

/**
 * 
 */
@Injectable({
  providedIn: 'root'
  , useFactory: RasterSeriesService.singleton
  , deps: [HttpClient]
})
export class RasterSeriesService {

  private constructor(private delegate: SeriesDelegate) {

  }
  /**
   * 
   */ 
  public static singleton(http: HttpClient): RasterSeriesService {
    return new RasterSeriesService(new InternalRasterSeriesDelegate());
  }
  
  /**
   * 
   */
  public getSeries(rasterId: number): Observable<RasterSeries> {
    const result = this.delegate.getSeries(rasterId);
    return result;
  }
}
