import {CommonModule} from '@angular/common';
import {NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from '@angular/core';
import {DatasetItemComponent} from 'src/pages/datasets/item/datasetitem.component';
import {ModelsItemComponent} from 'src/pages/models/item/models.component';
import {DataSourceList} from 'src/pages/main/components/datasourcelist/datasourcelist.component';
import {MapComponent} from 'src/pages/main/components/map/map.component';
import {PropertiesComponent} from 'src/pages/main/components/properties/properties.component';
import {RoiQueryBarComponent} from 'src/pages/main/components/roiquerybar/roiquerybar.component';
import {PointsPlotLegendComponent} from 'src/pages/main/components/pointsplotlegend/pointsplotlegend.component';
import {RasterSeriesPlots} from 'src/pages/main/components/rasterseriesplots/rasterseriesplots.component';

@NgModule({
  declarations: [
    DataSourceList
    , MapComponent
    , PropertiesComponent
    , RoiQueryBarComponent
    , PointsPlotLegendComponent
    , RasterSeriesPlots
    , DatasetItemComponent 
    , ModelsItemComponent
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
    , DatasetItemComponent 
    , ModelsItemComponent
  ]
})
export class ComponentsModule {

}