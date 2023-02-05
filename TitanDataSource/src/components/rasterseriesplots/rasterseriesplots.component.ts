import {Component, ViewEncapsulation, OnInit} from '@angular/core';
import {AddPointManager} from 'src/services/addpoints/AddPointManager';
import {PointAndRasterAssociations} from 'src/services/rasterassociations/PointAndRasterAssociations';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {QueryPoint} from 'src/core/rasters/QueryPoint';
import {RasterSeriesService} from 'src/services/rasterseries/RasterSeriesService';
import {RastersVisibilityService} from 'src/services/rasterstates/RastersVisibilityService';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {AssociationsChartSeries} from './AssociationsChartSeries';
import {Objects} from 'src/core/types/Objects';

declare var $: any;
@Component({
  selector: 'rasterseriesplots',
  templateUrl: './rasterseriesplots.component.html',
  styleUrls: ['../../app/app.component.css', './rasterseriesplots.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RasterSeriesPlots implements OnInit {
  private chart: any;
  private handlers: Map<number, AssociationsChartSeries> = new Map();

  /**
   * 
   */
  public constructor(private manager: AddPointManager,
    private associations: PointAndRasterAssociations,
    private service: RastersService,
    private seriesService: RasterSeriesService,
    private visibilityService: RastersVisibilityService
  ) {

  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.initChart();
    this.manager.getQueryPoints().subscribe(this.onQueryPointsChanged.bind(this));
    this.manager.getSelectedQueryPoint().subscribe(this.onSelectedPoint.bind(this)); 
    this.service.getSelectedItem().subscribe(this.onSelectedItem.bind(this));
  }
  
  
  /**
   * 
   */
  private onSelectedPoint(qp:QueryPoint):void  {
    this.resetSelectedItem(); 
  }

  /**
   * 
   */
  private onSelectedItem(item: any) {
    if (item instanceof RasterParameter) {
      this.selectPointOnChart(<RasterParameter> item);
    }
  }

  /**
   * 
   */
  private selectPointOnChart(item: RasterParameter): void {
    this.chart.series.forEach((s: any) => {
      const index = s.data.findIndex((p: any) => p.param === item);
      if (index !== -1 && s.visible) {
        const point = s.data[index];
        point.select(true);
      }
    });
  }

  /**
   * 
   */
  private onQueryPointsChanged(points: QueryPoint[]): void {
    points.forEach(this.addAssociationsChartSeries.bind(this));
    this.resetSelectedItem(); 
  }
  
  /**
   * 
   */
  private resetSelectedItem():void {
    setTimeout(() => {
      const item = this.service.getSelectedItemValue(); 
      this.onSelectedItem(item);
    })
  }

  /**
   * 
   */
  private addAssociationsChartSeries(point: QueryPoint): void {
    if (!this.handlers.has(point.id)) {
      const handler = new AssociationsChartSeries( //
        point.id, this.seriesService, this.associations, this.manager);
      if (Objects.isNotNull(this.chart)) {
        handler.init(this.chart);
      }
      this.handlers.set(point.id, handler);
    }
  }

  /**
  * 
  */
  private onChartReady(c: Object) {
    this.chart = c;
    this.handlers.forEach(c => c.init(this.chart));
  }

  /**
   * 
   */
  private initChart() {
    const div = $("#plot-area");
    const o = $("<div></div>");
    div.append(o);
    o.highcharts(this.getChartsConfig(div));
  }

  /**
   * 
   */
  private getChartsConfig(div: any): any {
    const result = {
      series: []
      , tooltip: {
        headerFormat: '<span style="font-size: 10px">',
        pointFormat: '<b>{point.y}</b>'
      }
      , plotOptions: {
        series: {
          cursor: 'pointer'
          , events: {
            click: this.onSeriesPointClicked.bind(this)
          }
          , allowPointSelect: true
          , marker: {
            states: {
              select: {
                fillColor: '#FF0000',
                lineWidth: 3,
                lineColor: '#FF0000'
              }
            }
          }
        }
      }
      , title: ""
      , chart: {
        type: 'line'
        , backgroundColor: null
        , borderWidth: 0
        , margin: [2, 4, 2, 4]
        , height: div.height() - 20
        , style: {
          overflow: 'visible'
        }
        , events: {
          load: (evt: any) => this.onChartReady(evt.target)
        }
        , animation: false
      }
    };
    return result;
  }

  /**
   * 
   */
  private onSeriesPointClicked(event: any): void {
    setTimeout(() => this.setVisibilityBasedOnClickEvent(event));
  }

  /**
   * 
   */
  private setVisibilityBasedOnClickEvent(event: any) {
    const param = event.point.param;
    const newvalue = !this.visibilityService.getVisibility(param).value;
    this.visibilityService.getVisibility(param).next(newvalue);
  }
}



