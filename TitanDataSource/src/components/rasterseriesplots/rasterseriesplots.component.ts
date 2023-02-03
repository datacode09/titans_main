import {Component, ViewEncapsulation, OnInit} from '@angular/core';
import {AddPointManager} from 'src/services/addpoints/AddPointManager';
import {PointAndRasterAssociations} from 'src/services/rasterassociations/PointAndRasterAssociations';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {QueryPoint} from 'src/core/rasters/QueryPoint';
import {Subscription, Observable} from 'rxjs';
import {Objects} from 'src/core/types/Objects';
import {RasterSeriesService} from 'src/services/rasterseries/RasterSeriesService';
import {RasterSeries} from 'src/core/rasters/RasterSeries';

declare var $: any;
@Component({
  selector: 'rasterseriesplots',
  templateUrl: './rasterseriesplots.component.html',
  styleUrls: ['../../app/app.component.css', './rasterseriesplots.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RasterSeriesPlots implements OnInit {
  private chart: any;
  private series: [];
  private handlers: AssociationsHandler[] = [];

  /**
   * 
   */
  public constructor(private manager: AddPointManager,
    private associations: PointAndRasterAssociations,
    private service: RastersService,
    private seriesService: RasterSeriesService
  ) {
  }


  public ngOnInit(): void {
    this.initChart();
    this.manager.getQueryPoints().subscribe(this.onQueryPointsChanged.bind(this));
    this.manager.getSelectedQueryPoint().subscribe(this.onQueryPointSelected.bind(this));
  }


  /**
   * 
   */
  private onQueryPointSelected(queryPoint: QueryPoint): void {
    this.handlers.forEach(h => h.setActiveIfMatching(queryPoint.id));
    this.handlers.filter(h => h.isActive())
      .forEach(handler => {
        handler.subscribe(this.associations.getAssociations(queryPoint.id));
      });
  }

  /**
   * 
   */
  private onQueryPointsChanged(points: QueryPoint[]): void {
    this.handlers.forEach(h => h.unsubscribe());
    points.forEach((p) => {
      const handler = new AssociationsHandler( //
        this.seriesService, this.chart, p.id);
      handler.subscribe(this.associations.getAssociations(p.id));
      this.handlers.push(handler);
    });
  }

  /**
  * 
  */
  private onChartReady(c: Object) {
    this.chart = c;
  }

  /**
   * 
   */
  private initChart() {
    const div = $("#plot-area");
    const o = $("<div></div>");
    div.append(o);
    o.highcharts({
      series: this.series,
      tooltip: {
        headerFormat: '<span style="font-size: 10px">',
        pointFormat: '<b>{point.y}</b>'
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
    });
  }
}

class AssociationsHandler {

  private subscription: Subscription;
  private series: Map<number, any> = new Map();
  private active: boolean = false;

  /**
   * 
   */
  public constructor( //
    private seriesservice: RasterSeriesService,
    private chart: any, private pointId: number) {
  }

  /**
   * 
   */
  public isActive(): boolean {
    return this.active;
  }


  /**
   * 
   */
  public setActiveIfMatching(id: number): void {
    this.active = id === this.pointId;
    this.show(this.active);
  }

  /**
   * 
   */
  private onAssociationsChanged(arr: number[]) {
    if (this.isActive) {
      this.deleteAllSeries();
    }
    arr.map(this.toSeries.bind(this)).forEach((s: any) => {
      const seriesId = Number(s.id.replace('series-', ""));
      this.series.set(seriesId, s)
    });
    this.show(this.active);
  }

  /**
   * 
   */
  private show(show: boolean): void {
    if (show) {
      this.series.forEach(s => {
        if (Objects.isNull(this.chart.get(s.id))) {
          this.chart.addSeries(s, true);
        } else {
          this.chart.get(s.id).setData(s.data);
        }
      });
    } else {
      this.deleteAllSeries();
    }
  }

  /**
   * 
   */
  private toSeries(rasterId: number): any {
    const data: any[] = [];
    this.seriesservice.getSeries(this.pointId, rasterId).subscribe(rasterSeries => {
      this.onSeriesLoaded(rasterId, rasterSeries);
    });
    const result = {
      id: 'series-' + rasterId,
      name: 'Raster ' + rasterId,
      data: data
    };
    return result;
  }

  /**
   * 
   */
  private onSeriesLoaded(rasterId: number, e: RasterSeries): void {
    if (e != null) {
      if (this.series.has(rasterId)) {
        const series = this.series.get(rasterId);
        const data = series.data;
        data.length = 0;
        e.toData().forEach(d => data.push(d));
      }
    }
    this.show(this.active);
  }

  /**
   * 
   */
  private deleteAllSeries(): void {
    this.series.clear();
    var seriesLength = this.chart.series.length;
    for (var i = seriesLength - 1; i > -1; i--) {
      this.chart.series[i].remove();
    }
  }

  /**
   * 
   */
  public subscribe(arg0: Observable<number[]>) {
    this.subscription = arg0.subscribe(this.onAssociationsChanged.bind(this));
  }

  /**
   * 
   */
  public unsubscribe(): void {
    if (Objects.isNotNull(this.subscription)) {
      this.subscription.unsubscribe();
      this.subscription = null;
    }
  }
}