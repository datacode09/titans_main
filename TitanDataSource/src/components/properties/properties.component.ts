import {Component, ViewEncapsulation, OnInit} from '@angular/core';
import {PropertyDetailsViewFactory} from './PropertyDetailsViewFactory';
import {RastersService} from 'src/services/rasterservices/RastersService';


@Component({
  selector: 'properties',
  templateUrl: './properties.component.html',
  styleUrls: ['../../app/app.component.css', './properties.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PropertiesComponent implements OnInit {

  public title: string = "Regions/Points of Interests";

  /**
   * 
   */
  public constructor(private service: RastersService) {
    this.service.getSelectedItem().subscribe(a => {
      const detailsView = PropertyDetailsViewFactory.get(a);
      this.title = detailsView.getTitle();
      const element = detailsView.getElement();
      $("#propertiesContainer").empty().append(element);
    });
  }

  /**
   * 
   */
  public ngOnInit(): void {

  }
}