import {Component, OnInit} from '@angular/core';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {RastersGroup} from 'src/core/rasters/RastersGroup';

@Component({
  selector: 'models',
  templateUrl: './models.component.html',
  styleUrls: ['../../app/app.component.css']
})
export class ModelsComponent implements OnInit {
  public datasets: any[] = [];

  /**
   * 
   */
  public constructor(private service: RastersService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    
  }
  
  /**
   * 
   */
  private onRastersChanged(arr: RastersGroup[]): void {
    this.setDatasets(arr); 
  }


  /**
   * 
   */
  private setDatasets(arr: RastersGroup[]): void {
    this.datasets = arr.map(this.toDetailsView.bind(this));
  }

  /**
   * 
   */
  private toDetailsView(a: RastersGroup): any {
    const result = {
      text: a.name
      , description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Fames ac turpis egestas sed tempus urna et pharetra pharetra. Eu volutpat odio facilisis mauris sit amet massa vitae. Gravida neque convallis a cras semper auctor neque. Nisi est sit amet facilisis. Eget nullam non nisi est sit amet facilisis magna etiam. Neque convallis a cras semper auctor. Condimentum id venenatis a condimentum. Nibh sit amet commodo nulla facilisi nullam. Non curabitur gravida arcu ac tortor dignissim convallis aenean et. Quam elementum pulvinar etiam non quam lacus suspendisse faucibus interdum. Diam vel quam elementum pulvinar etiam non quam. Odio pellentesque diam volutpat commodo sed. \n"
        + "Faucibus a pellentesque sit amet porttitor eget dolor morbi non. In nulla posuere sollicitudin aliquam ultrices sagittis. Cursus vitae congue mauris rhoncus aenean vel. Dis parturient montes nascetur ridiculus mus. Nisi lacus sed viverra tellus in hac habitasse. Vulputate dignissim suspendisse in est ante in nibh mauris cursus. Luctus accumsan tortor posuere ac ut. A arcu cursus vitae congue mauris rhoncus aenean. Amet consectetur adipiscing elit duis tristique sollicitudin. Mollis aliquam ut porttitor leo a diam. Platea dictumst quisque sagittis purus sit amet. Mi tempus imperdiet nulla malesuada pellentesque elit. Sem et tortor consequat id porta nibh venenatis cras. Pharetra pharetra massa massa ultricies mi quis hendrerit dolor. Felis donec et odio pellentesque diam volutpat."
    };
    return result;
  }

}

