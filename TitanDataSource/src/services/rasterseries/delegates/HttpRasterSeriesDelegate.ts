import {SeriesDelegate} from './SeriesDelegate';
import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {Observable, BehaviorSubject} from 'rxjs';
import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {Objects} from 'src/core/types/Objects';

/**
 * 
 */
export class HttpRasterSeriesDelegate implements SeriesDelegate {
  private baseUrl: string = "http://localhost:8081/titansdata.web"
  /**
   * 
   */
  public constructor(private http: HttpClient) {
  }

  /**  
   * 
   */
  public getSeries(rasterId: number, point: any, params: RasterParameter[]): Observable<RasterSeries> {
    const result: BehaviorSubject<RasterSeries> = new BehaviorSubject(null);
    this.loadSeries(rasterId, point, params).subscribe(e => {
      if (e != null) {
        result.next(e);
      }
    });
    return result;
  }

  /**
   * 
   */
  private loadSeries(rasterId: number, point: any, rasterparams: RasterParameter[]): Observable<RasterSeries> {
    const url = this.baseUrl + "/getRasterPointValues";
    const params = new HttpParams()
      .set("rasterId", rasterId.toString())
      .set("point", "POINT(" + point.x + " " + point.y + ")")
      .set("srid", "3857")
      .set("parameters", JSON.stringify(rasterparams.map(e => {
        return {
          key: e.parameter.key
          , parentKey: e.parameter.parentKey
        }
      })))
      ;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    const result: BehaviorSubject<RasterSeries> = new BehaviorSubject(null);
    this.http.get(url, {params: params, headers: headers}).subscribe((response: any) => {
      const rasterSeries = this.toRasterSeries(rasterId, response, rasterparams);
      result.next(rasterSeries);
    });
    return result;
  }

  /**
   * 
   */
  private toRasterSeries(rasterId: number, response: any, rasterparams: RasterParameter[]): RasterSeries {
    const keys = Object.keys(response.values);
    const values = keys.map((k, i) => {


      const index = rasterparams.findIndex(f => f.parameter.key === k);
      const param = rasterparams[index];
      const t = param.parameter.validtime;
      let x: any = i;
      if (Objects.isNotNull(t)) {
        const year = Number(t.substr(0, 4));
        const month = Number(t.substr(4, 2)) - 1;
        const day = Number(t.substr(6, 2));
        const hour = Number(t.substr(8, 2));
        x = new Date(year, month, day, hour, 0);
      }
      const result = {
        x: x
        , y: response.values[k]
        , param: param
      };
      return result;
    });
    const result = new RasterSeries(rasterId, values.sort((x1, x2)=> x1.x - x2.x));
    return result;
  }
}

