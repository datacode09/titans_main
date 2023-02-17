import {Injectable} from '@angular/core';
import {Observable, BehaviorSubject, Subject} from 'rxjs';
import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {HttpClient} from '@angular/common/http';
import {InternalRasterSeriesDelegate} from './delegates/InternalRasterSeriesDelegate';
import {SeriesDelegate} from './delegates/SeriesDelegate';
import {AddPointManager} from '../addpoints/AddPointManager';
import {QueryPoint} from 'src/core/rasters/QueryPoint';
import {RastersService} from '../rasterservices/RastersService';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {HttpRasterSeriesDelegate} from './delegates/HttpRasterSeriesDelegate';
import {httpsources} from 'src/conf/conf.json';
import {RasterParamsService} from '../rasterservices/RasterParamsService';
/**
 * 
 */
@Injectable({
  providedIn: 'root'
  , useFactory: RasterSeriesService.singleton
  , deps: [HttpClient, RastersService, RasterParamsService, AddPointManager]
})
export class RasterSeriesService {
  private series: Map<string, Observable<RasterSeries>> = new Map();

  /**
   * 
   */
  public static singleton(http: HttpClient, 
    s: RastersService, 
    p: RasterParamsService, 
    m: AddPointManager): RasterSeriesService {
    let delegate;
    if (httpsources) {
      delegate = new HttpRasterSeriesDelegate(http);
    } else {
      delegate = new InternalRasterSeriesDelegate();
    }
    const instance = new RasterSeriesService(delegate, s, p, m);
    return instance;
  }
  
  
  /**
   * 
   */
  private constructor(private delegate: SeriesDelegate, 
    private rastersService: RastersService, 
    private paramService: RasterParamsService,
    private m: AddPointManager) {

  }

  /**
   * 
   */
  public getSeries(pointId: number, rasterId: number): Observable<RasterSeries> {
    const key = pointId + "-" + rasterId;
    if (!this.series.has(key)) {
      const p: QueryPoint = this.m.getQueryPoint(pointId);
      const mapPoint = p.mapPoint;
      const params: RasterParameter[] = this.paramService.getParametersValue(rasterId);
      const observable = this.delegate.getSeries(rasterId, mapPoint, params);
      this.series.set(key, observable);
    } else {
      const observable = <BehaviorSubject<RasterSeries>> this.series.get(key);
      const value = observable.value;
      setTimeout(() => {
        observable.next(null);
        observable.next(value);
      });
    }
    const result = this.series.get(key);
    return result;
  }
}
