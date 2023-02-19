import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {httpsources} from 'src/conf/conf.json';
import {ParamDelegate} from './delegates/ParamDelegate';
import {HttpParamDelegate} from './delegates/HttpParamDelegate';
import {Clazz} from 'src/core/rasters/Clazz';
import {Observable, BehaviorSubject} from 'rxjs';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {RastersService} from './RastersService';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {RasterEntity} from 'src/core/rasters/RasterEntity';


@Injectable({
  providedIn: 'root'
  , useFactory: RasterParamsService.singleton
  , deps: [RastersService, HttpClient]
})
export class RasterParamsService {

  private rasterParameters: Map<number, BehaviorSubject<RasterParameter[]>> = new Map();
  private selectedClasses: BehaviorSubject<Map<string, Clazz>> = new BehaviorSubject(new Map());
  private rasterClasses: Map<number, BehaviorSubject<Map<string, Clazz>>> = new Map();
  private allRasterClasses: Map<number, BehaviorSubject<Map<string, Clazz[]>>> = new Map();


  /**
   * 
   */
  public static singleton(rastersService: RastersService, http: HttpClient): RasterParamsService {
    let result: RasterParamsService;
    if (httpsources) {
      result = new RasterParamsService(rastersService, new HttpParamDelegate(http));
    } else {
      throw new Error("Not yet supported");
    }
    return result;
  }

  /**
   * 
   */
  public constructor(private rastersService: RastersService, private delegate: ParamDelegate) {
    this.rastersService.getRasters().subscribe(this.onRastersChanged.bind(this));
    this.rastersService.getSelectedRaster().subscribe(this.onRasterSelected.bind(this));

  }

  /**
   * 
   */
  private onRasterSelected(raster: RasterEntity): void {
    let val;
    console.log(raster); 
    if (raster != null) {
      const subj = this.rasterClasses.get(raster.rasterId);
      val = subj.getValue();
    } else {
      val = new Map();
    }
    this.selectedClasses.next(val);
  }

  /**
   * 
   */
  private onRastersChanged(arr: RastersGroup[]): void {
    arr.forEach(g => {
      g.rasterIds.forEach(this.initRasterParameters.bind(this));
    });
  }

  /**
   * 
   */
  public getSelectedClasses(): Observable<Map<string, Clazz>> {
    return this.selectedClasses;
  }

  /**
   * 
   */
  private initRasterParameters(rasterId: number): void {
    if (!this.rasterParameters.has(rasterId)) {
      this.rasterParameters.set(rasterId, new BehaviorSubject([]));
    }
    if (!this.rasterClasses.has(rasterId)) {
      const sub = new BehaviorSubject(new Map());
      sub.subscribe((m) => this.onRasterClassesChanged(rasterId, m));
      this.rasterClasses.set(rasterId, sub);
    }
    if (!this.allRasterClasses.has(rasterId)) {
      const sub = new BehaviorSubject(new Map());
      sub.subscribe(m => this.onAllRasterClassesChanged(rasterId, m));
      this.allRasterClasses.set(rasterId, sub);
    }
    this.delegate.loadRasterClasses(rasterId).subscribe((m) => this.onRasterClassesLoaded(rasterId, m));
  }

  /**
   * 
   */
  private onRasterClassesLoaded(rasterId: number, m: Map<string, Clazz[]>): void {
    const obs = this.allRasterClasses.get(rasterId);
    obs.next(m);
  }

  /**
   * 
   */
  public onAllRasterClassesChanged(rasterId: number, map: Map<string, Clazz[]>): void {
    const newclasses: Map<string, Clazz> = new Map();
    map.forEach((v, k) => {
      newclasses.set(k, v[0]);
    });
    this.rasterClasses.get(rasterId).next(newclasses);
  }

  /**
   * 
   */
  private onRasterClassesChanged(rasterId: number, clazzes: Map<string, Clazz>) {
    if (clazzes.size > 0) {
      this.delegate.loadRasterParameters(rasterId, clazzes).subscribe((params: RasterParameter[]) => {
        this.rasterParameters.get(rasterId).next(params);
      });
    } else {
      this.rasterParameters.get(rasterId).next([]);
    }
  }

  /**
   * 
   */
  public getParameters(rasterId: number): Observable<RasterParameter[]> {
    return this.rasterParameters.get(rasterId);
  }

  /**
   * 
   */
  public getParametersValue(rasterId: number): RasterParameter[] {
    return this.rasterParameters.get(rasterId).value
  }
}

