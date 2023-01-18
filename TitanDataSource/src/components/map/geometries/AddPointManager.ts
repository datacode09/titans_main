import {Injectable, OnInit} from '@angular/core';
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import {Observable} from 'rxjs';
import {QueryPoint} from './QueryPoint';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {Objects} from 'src/core/types/Objects';

declare var esri: any;

@Injectable({
  providedIn: 'root'
  //  , useFactory: 
  , deps: [RastersService]
})
export class AddPointManager {

  public selectQueryPoint: BehaviorSubject<QueryPoint> = new BehaviorSubject(null);
  private map: any;
  private queryPoints: BehaviorSubject<QueryPoint[]> = new BehaviorSubject([]);
  private graphicsLayer: any;

  public constructor(private rastersService: RastersService) {

  }

  /**
   * 
   */
  private init(): void {
    this.initGraphicsLayer();
    this.queryPointActivated.subscribe(this.syncMapCursor.bind(this));
    this.queryPoints.subscribe(this.addPointsToMap.bind(this));
    $(document).on('keydown', e => {
      if (e.key == "Escape") {
        this.queryPointActivated.next(false);
      }
    });
    this.map.addLayer(this.graphicsLayer);
    this.map.on("click", this.handleClickEvent.bind(this));
    this.getSelectedQueryPoint().subscribe(this.onSelectedQueryPointChanged.bind(this));
    this.getSelectedQueryPoint().subscribe((p) => {
      if (p !== null) {
        this.rastersService.setSelectedItem(p);
      }
    });
    this.rastersService.getSelectedItem().subscribe(e => {
      if (!(e instanceof QueryPoint)) {
        this.setSelectedQueryPoint(null);
      } else {
        this.setSelectedQueryPoint(<QueryPoint> e);
      }
    });
  }

  /**
   * 
   */
  private initGraphicsLayer(): void {
    this.graphicsLayer = new esri.layers.GraphicsLayer();
    this.graphicsLayer.on("click", this.onGraphicLayerClicked.bind(this));
    this.graphicsLayer.on("mouse-over", (e: any) => {
      this.map.setMapCursor("pointer");
    });
    this.graphicsLayer.on("mouse-out", (e: any) => {
      this.map.setMapCursor("default");
    });
  }

  private onGraphicLayerClicked(event: any): void {
    const graphic = event.graphic;
    if (Objects.isNotNull(graphic)) {
      const id = graphic.id;
      const index = this.queryPoints.getValue().findIndex(p => p.id === id);
      if (index > -1) {
        const p = this.queryPoints.getValue()[index];
        this.selectQueryPoint.next(p);
      }
    }
  }


  /**
   * 
   */
  private onSelectedQueryPointChanged(newvalue: QueryPoint): void {
    const graphics = this.graphicsLayer.graphics;
    graphics.forEach((g: any) => {
      g.symbol.color.a = 0.3;
    });
    if (newvalue !== null) {
      const index = graphics.findIndex((g: any) => g.id === newvalue.id);
      if (index >= 0) {
        const graphic = graphics[index];
        graphic.symbol.color.a = 1.0;
      }
    }

    this.graphicsLayer.redraw();
  }

  /**
   * 
   */
  public getQueryPoints(): Observable<QueryPoint[]> {
    return this.queryPoints;
  }

  /**
   * 
   */
  public getSelectedQueryPoint(): Observable<QueryPoint> {
    return this.selectQueryPoint;
  }

  /**
   * 
   */
  public setSelectedQueryPoint(newvalue: QueryPoint): void {
    const currentvalue = this.selectQueryPoint.value;
    if (!(currentvalue === null && newvalue === null)) {
      if (newvalue !== null && currentvalue === null) {
        this.selectQueryPoint.next(newvalue);
      } else if (newvalue === null && currentvalue !== null) {
        this.selectQueryPoint.next(newvalue);
      } else if (newvalue.id !== currentvalue.id) {
        this.selectQueryPoint.next(newvalue);
      }
    }
  }

  /**
   * 
   */
  private handleClickEvent(e: any) {
    if (this.queryPointActivated.value) {
      const view: any[] = [];
      const queryPoint = QueryPoint.create(e.mapPoint, view);
      view.push({
        type: 'label',
        name: "ID",
        value: "Point "  + queryPoint.id
      });
      view.push({
        type: 'label',
        name: "Point",
        value: e.mapPoint.x.toFixed(0) + ", " + e.mapPoint.y.toFixed(0)
      });
      const newarr: QueryPoint[] = this.queryPoints.value.concat(queryPoint);
      this.queryPoints.next(newarr);
      this.setQueryPointActivated(false);
    }
  }

  /**
   * 
   */
  private addPointsToMap(arr: QueryPoint[]): void {
    arr.filter(this.isNotInMap.bind(this))
      .forEach(this.addPointToMap.bind(this));
  }


  /**
   * 
   */
  private isNotInMap(queryPoint: QueryPoint): boolean {
    const index = this.graphicsLayer.graphics //
      .findIndex((g: any) => g.id === queryPoint.id);
    const result = index === -1;
    return result;
  }

  /**
   * 
   */
  private addPointToMap(p: QueryPoint): void {
    const pt = p.mapPoint;
    var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(
      esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(
        new esri.Color([255, 0, 0, 0.5]));
    var graphic = esri.Graphic(pt, sms);
    graphic.id = p.id;
    this.graphicsLayer.add(graphic);
  }

  /**
   * 
   */
  private syncMapCursor(b: boolean): void {
    if (b) {
      const img = "/assets/images/red_dot.png";
      this.map.setMapCursor("url(" + img + "),auto");
    } else {
      this.map.setMapCursor("default");
    }
  }

  private queryPointActivated: BehaviorSubject<boolean> = new BehaviorSubject(false);
  /**
   * 
  */
  public getQueryPointActivated(): Observable<boolean> {
    return this.queryPointActivated;
  }

  /**
   * 
   */
  public setQueryPointActivated(value: boolean): void {
    this.queryPointActivated.next(value);
  }

  /**
   * 
   */
  public toggleQueryPointActivated(): void {
    const currentValue = this.queryPointActivated.getValue();
    this.queryPointActivated.next(!currentValue);
  }

  /**
   * 
   */
  public setMap(map: any) {
    this.map = map;
    this.init();

  }
}