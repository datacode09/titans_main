import {Component, OnInit} from '@angular/core';

declare var dojo: any;
declare var esri: any;
declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'titans.datasource';
  private chart: any;

  public ngOnInit(): void {
    this.initChart();
  }

  /**
   * 
   */
  private onChartReady(c: Object) {
    this.chart = c;
    //    this.addLayersToPlot();
    //    this.plotService.onChartReady(c);
  }

  private initChart() {
    const div = $("#plot-area");
    const o = $("<div></div>");
    div.append(o);
    console.log(o);
    o.highcharts({
      series: [{
        data: [[0, 0], [1, -2], [2, 3], [4, 2], [5, 1]]
      }],
      tooltip: {
        headerFormat: '<span style="font-size: 10px">',
        pointFormat: '<b>{point.y}</b> A'
      },
      chart: {
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
      }
    });
  }
}
