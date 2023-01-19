import {Objects} from 'src/core/types/Objects';
import {AddPointManager} from 'src/services/addpoints/AddPointManager';
import {QueryPoint} from 'src/core/rasters/QueryPoint';
declare var esri: any;

/**
 * 
 */
export class PointLayer {
  
  private map: any;
  private graphicsLayer: any;
  
  public constructor(private manager: AddPointManager) {
  }

  /**
   * 
   */
  public setMap(map: any) {
    this.map = map;
    this.init();
  }

  private init(): void {
    this.map.addLayer(this.graphicsLayer);
    this.map.on("click", this.handleClickEvent.bind(this));
    this.initGraphicsLayer();
    this.manager.getQueryPointActivated().subscribe(this.syncMapCursor.bind(this));
    this.manager.getQueryPoints().subscribe(this.addPointsToMap.bind(this));
    $(document).on('keydown', e => {
      if (e.key == "Escape") {
        this.manager.setQueryPointActivated(false);
      }
    });
    this.manager.getSelectedQueryPoint() //
    .subscribe(this.onSelectedQueryPointChanged.bind(this));
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
    this.map.addLayer(this.graphicsLayer);
  }


  private onGraphicLayerClicked(event: any): void {
    const graphic = event.graphic;
    if (Objects.isNotNull(graphic)) {
      const id = graphic.id;
      const index = this.manager.getQueryPointsValue().findIndex(p => p.id === id);
      if (index > -1) {
        const p = this.manager.getQueryPointsValue()[index];
        this.manager.setSelectedQueryPoint(p);
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
  private handleClickEvent(e: any) {
    if (this.manager.getQueryPointActivatedValue()) {
      this.addNewQueryPointFromMouseClick(e);
      this.manager.setQueryPointActivated(false);
    }
  }

  /**
   * 
   */
  private addNewQueryPointFromMouseClick(e: any): void {
    const newquerypoint = this.createQueryPoint(e.mapPoint);
    const currQueryPts = this.manager.getQueryPointsValue();
    const newarr: QueryPoint[] = currQueryPts.concat(newquerypoint);
    this.manager.setQueryPoints(newarr);
  }

  /**
   * 
   */
  private createQueryPoint(mapPoint: any): QueryPoint {
    const view: any[] = [];
    const newquerypoint = QueryPoint.create(mapPoint, view);
    view.push({
      type: 'label',
      name: "ID",
      value: "Point " + newquerypoint.id
    });
    view.push({
      type: 'label',
      name: "Point",
      value: mapPoint.x.toFixed(0) + ", " + mapPoint.y.toFixed(0)
    });
    return newquerypoint;
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

}


