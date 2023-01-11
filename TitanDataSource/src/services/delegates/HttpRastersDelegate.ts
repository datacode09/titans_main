import {RastersDelegate} from './RastersDelegate';
import {RastersService} from '../RastersService';
import {RastersGroup} from '../RastersGroup';
import {RasterEntity} from '../RasterEntity';
import {HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import {Observable, BehaviorSubject} from 'rxjs';
import {RasterImage} from '../RasterImage';
import {map} from 'rxjs/operators';
import {Envelope} from '../Envelope';

export class HttpRastersDelegate implements RastersDelegate {

  private host: RastersService;

  /**
   * 
   */
  public constructor(private http: HttpClient) {
  }

  /**
   * 
   */
  public setHost(host: RastersService): void {
    this.host = host;
  }

  /**
   * 
   */
  public loadRasters(): void {
    const url = "http://localhost:8081/titansdata.web/getRasters";
    const params = new HttpParams().set("userId", "0");
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    this.http.get(url, {params: params, headers: headers}).subscribe((response: any) => {
      const values = JSON.parse(response.values).rastergroups;
      const arr = values.map((value: any) => {
        const id = value.rasterGroupId;
        const name = value.name;
        const rasterIds: number[] = value.rasterIds
        const g = new RastersGroup(id, name, rasterIds);
        return g;
      });
      this.host.rasterGroups.next(arr);
    });
  }

  /**
   * 
   */
  public loadRasterEntity(rasterId: number, callback: any): void {
    const url = "http://localhost:8081/titansdata.web/getRaster";
    const params = new HttpParams()
      .set("rasterId", rasterId.toString())
      ;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    this.http.get(url, {params: params, headers: headers}).subscribe((response: any) => {
      const v: any = response.value;
      const e = this.mapToEntity(v);
      this.host.rastersmap.set(v.rasterId, e);
    });
  }

  /**
   * 
   */
  private mapToEntity(value: any): RasterEntity {
    const e = new RasterEntity(value.rasterId);
    return e;
  }

  /**
   * 
   */
  public getRasterImage(rasterId: number): Observable<RasterImage> {
    const url = "http://localhost:8081/titansdata.web/getRasterImage";
    const params = new HttpParams()
      .set("rasterId", rasterId.toString());
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    const result: BehaviorSubject<RasterImage> = new BehaviorSubject(null);
    this.http.get(url, {
      params: params,
      headers: headers
    }).subscribe((response: any) => {
      result.next(this.toRasterImage(response));
    });
    return result;
  }

  /**
   * 
   */
  private toRasterImage(response: any): RasterImage {
    const value = response.value;
    const imageUrl = value.imageURL;
    const xmin = value.lowerLeft.x;
    const xmax = value.upperRight.x;
    const ymin = value.lowerLeft.y;
    const ymax = value.upperRight.y;
    const srid = value.srid;
    const envelope: Envelope = new Envelope(xmin, xmax, ymin, ymax, srid);
    const result: RasterImage = new RasterImage(imageUrl, envelope);
    return result;
  }

}
