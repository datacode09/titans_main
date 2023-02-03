import {Subscription, Observable} from 'rxjs';
import {RasterSeriesService} from 'src/services/rasterseries/RasterSeriesService';
import {Objects} from 'src/core/types/Objects';
import {RasterSeries} from 'src/core/rasters/RasterSeries';
import {PointAndRasterAssociations} from 'src/services/rasterassociations/PointAndRasterAssociations';
import {AddPointManager} from 'src/services/addpoints/AddPointManager';
import {QueryPoint} from 'src/core/rasters/QueryPoint';

export class AssociationsChartSeries {

  private subscription: Subscription;
  private series: Map<string, any> = new Map();
  private active: boolean = false;
  private chart: any;

  /**
   * 
   */
  public constructor( //
    private pointId: number,
    private seriesservice: RasterSeriesService,
    private associations: PointAndRasterAssociations,
    private manager: AddPointManager
  ) {
  }

  /**
   * 
   */
  public init(chart: any) {
    if (Objects.isNull(this.chart)) {
      this.chart = chart;
      
      this.associations.getAssociations(this.pointId)
        .subscribe(this.onAssociationsChanged.bind(this));
      this.manager.getSelectedQueryPoint().subscribe(this.onSelectedQueryPoint.bind(this)); 
    }
  }
  
  
  /**
   * 
   */
  private onSelectedQueryPoint(qp:QueryPoint):void {
    this.series.forEach(this.setSeriesVisibility.bind(this)); 
  }
  
  /**
   * 
   */
  private isSeriesVisible() {
    const qp = this.manager.getSelectedQueryPointValue();
    const isActive = Objects.isNotNull(qp) && qp.id === this.pointId;
    return isActive;
  }
  
  /**
   * 
   */
  private setSeriesVisibility(seriesObject:any):void {
    const visibility: boolean = this.isSeriesVisible();
    seriesObject.setVisible(visibility); 
  }

  /**
   * 
   */
  private onAssociationsChanged(rasterIds: number[]) {
    rasterIds.map(this.toChartSeries.bind(this))
      .forEach(this.addChartSeries.bind(this));
  }

  /**
   * 
   */
  private addChartSeries(chartSeries: any): void {
    const seriesId = chartSeries.id;
    const seriesObject = this.chart.addSeries(chartSeries);
    this.series.set(seriesId, seriesObject);
    this.setSeriesVisibility(seriesObject);
  }

  /**
   * 
   */
  private toChartSeries(rasterId: number): any {
    const data: any[] = [];
    this.seriesservice.getSeries(this.pointId, rasterId).subscribe(rasterSeries => {
      this.onSeriesLoaded(rasterId, rasterSeries);
    });
    const result = {
      id: this.getSeriesId(rasterId),
      name: 'Raster ' + rasterId,
      data: data
    };
    return result;
  }

  /**
   * 
   */
  private getSeriesId(rasterId: number): string {
    return 'series-' + this.pointId + '-' + rasterId;
  }

  /**
   * 
   */
  private onSeriesLoaded(rasterId: number, e: RasterSeries): void {
    if (e != null) {
      this.setSeriesData(rasterId, e.toData());
    }
  }

  /**
   * 
   */
  private setSeriesData(rasterId: number, newdata: any[]) {
    const seriesId = this.getSeriesId(rasterId);
    const seriesObject = this.series.get(seriesId);
    seriesObject.setData(newdata);
  }
}