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
import {ApiItemComponent} from 'src/pages/exportapi/apicomponent/apiitem.component';
import {ProjectsGeographyComponent} from 'src/pages/projects/components/projectgeography/projectsgeography.component';
import {ProjectsListComponent} from 'src/pages/projects/components/projectslist/projectslist.component';
import {ProjectDataSourcesComponent} from 'src/pages/projects/components/projectdatasources/projectdatasources.component';

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
    , ApiItemComponent
    , ProjectsGeographyComponent
    , ProjectsListComponent
    , ProjectDataSourcesComponent
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
    , ApiItemComponent
    , ProjectsGeographyComponent
    , ProjectsListComponent   
    , ProjectDataSourcesComponent
  ]
})
export class ComponentsModule {

}