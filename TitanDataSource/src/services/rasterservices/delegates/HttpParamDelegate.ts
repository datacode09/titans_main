import {ParamDelegate} from './ParamDelegate';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Clazz} from 'src/core/rasters/Clazz';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {RasterParameter} from 'src/core/rasters/RasterParameter';

export class HttpParamDelegate implements ParamDelegate {

  private baseUrl: string = "http://localhost:8081/titansdata.web.dev";

  public constructor(private http: HttpClient) {
  }
  
  /**
   * 
   */
  public loadRasterClasses(rasterId: number): Observable<Map<string, Clazz[]>> {
    const url = this.baseUrl + "/getRasterClasses";
    const params = new HttpParams() //
      .set("rasterId", rasterId + "");
    const options = {params: params};
    const result = this.http.get(url, options) //
      .pipe(map<any, Map<string, Clazz[]>>(this.responseToClazzes.bind(this)));
    return result;
  }

  /**
   * 
   */
  private responseToClazzes(response: any): Map<string, Clazz[]> {
    const result = new Map();
    const map = response.clazzes.map;
    const keys = Object.keys(map);
    keys.forEach(k => {
      const arr = this.toClazzArray(map[k].myArrayList);
      result.set(k, arr);
    });
    return result;
  }

  /**
   * 
   */
  private toClazzArray(jsons: string[]): Clazz[] {
    const result = jsons //
      .map((s: string) => JSON.parse(s))
      .map(o => new Clazz(o.key, o))
      ;
    return result;
  }

  /**
   * 
   */
  public loadRasterParameters(rasterId: number, clazzes: Map<string, Clazz>)
    : Observable<RasterParameter[]> {
    const text = this.clazzesToJsonText(clazzes);
    const url = this.baseUrl + "/getRasterParameters";
    const params = new HttpParams() //
      .set("rasterId", rasterId + "")//
      .set("clazzes", text);
    const options = {params: params};
    const result = this.http.get(url, options) //
      .pipe(map<any, RasterParameter[]>( //
        (response: any) => this.responseToRasterParameters(rasterId, response)));
    return result;
  }

  /**
   * 
   */
  private clazzesToJsonText(clazzes: Map<string, Clazz>): string {
    const arr = Array.from(clazzes).map(k => k[1] === undefined ? "" : k[1].obj);
    const v = arr.map((a) => {
      if (a !== "") {
        let obj: any = {};
        Object.keys(a).forEach(k => {
          obj[k] = a[k][0];
        });
        return JSON.stringify(obj)
      } else {
        return "";
      }
    })
    const text = JSON.stringify(v);
    return text;
  }

  /**
   * 
   */
  private responseToRasterParameters(rasterId: number, response: any): RasterParameter[] {
    const result: RasterParameter[] = response.values.map((v: any) => {
      const map = v.map;
      const r = new RasterParameter(rasterId, map);
      return r;
    });
    return result;
  }
}