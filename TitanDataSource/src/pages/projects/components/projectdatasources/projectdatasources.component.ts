import {OnInit, Component} from '@angular/core';
import {DataSource} from '../DataSource';
import {ProjectService} from '../../ProjectService';
import {Project} from '../Project';
import {Subscription} from 'rxjs';
import {Objects} from 'src/core/types/Objects';
import {RastersService} from 'src/services/rasterservices/RastersService';


@Component({
  selector: "projectdatasources-root"
  , templateUrl: "./projectdatasources.component.html"
  , styleUrls: ["projectdatasources.component.css"]
})
export class ProjectDataSourcesComponent implements OnInit {
  private subscription: Subscription;
  public datasets: DataSource[] = [];
  
  /**
   * 
   */
  public constructor(private projectService: ProjectService, //
    private rastersService:RastersService) {
  }


  /**
   * 
   */
  public ngOnInit(): void {
    this.projectService.getSelectedProject().subscribe(this.onSelectedProjectChanged.bind(this));
  }

  /**
   * 
   */
  private onSelectedProjectChanged(p: Project): void {
    if (this.subscription != null) {
      this.subscription.unsubscribe();
    }
    if (Objects.isNotNull(p)) {
      this.subscription = this.projectService.getProjectData(p.id) //
        .subscribe(this.onProjectDataChanged.bind(this));
    }
  }

  /**
   * 
   */
  private onProjectDataChanged(data: DataSource[]): void {
    this.updateDataSourcesList(data);
  }

  /**
   * 
   */
  public updateDataSourcesList(data: DataSource[]): void {
    this.datasets = data;
  }
}