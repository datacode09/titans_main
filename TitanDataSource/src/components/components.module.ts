import {CommonModule} from '@angular/common';
import {NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from '@angular/core';
import {DataSourceList} from './datasourcelist/datasourcelist.component';
import {MapComponent} from './map/map.component';


@NgModule({
  declarations: [
    DataSourceList
    , MapComponent
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
  ]
})
export class ComponentsModule {

}