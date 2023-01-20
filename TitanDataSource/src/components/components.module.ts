import {CommonModule} from '@angular/common';
import {NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from '@angular/core';
import {DataSourceList} from './datasourcelist/datasourcelist.component';
import {MapComponent} from './map/map.component';
import {PropertiesComponent} from './properties/properties.component';
import {RoiQueryBarComponent} from './roiquerybar/roiquerybar.component';
import {PointsPlotLegendComponent} from './pointsplotlegend/pointsplotlegend.component';
import {RasterSeriesPlots} from './rasterseriesplots/rasterseriesplots.component';

@NgModule({
  declarations: [
    DataSourceList
    , MapComponent
    , PropertiesComponent
    , RoiQueryBarComponent
    , PointsPlotLegendComponent
    , RasterSeriesPlots
  ],
  imports: [
    CommonModule
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA,
    NO_ERRORS_SCHEMA
  ],
  exports: [
    DataSourceList
    , MapComponent
    , PropertiesComponent
    , RoiQueryBarComponent  
    , PointsPlotLegendComponent  
    , RasterSeriesPlots
  ]
})
export class ComponentsModule {

}