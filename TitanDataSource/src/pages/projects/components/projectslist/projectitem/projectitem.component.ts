import {OnInit, Component, Input} from '@angular/core';
import {Project} from '../../Project';
import {ProjectService} from 'src/pages/projects/ProjectService';


@Component({
  selector: "projectitem-root"
  , templateUrl: "./projectitem.component.html"
  , styleUrls: ["./projectitem.component.css"]
})
export class ProjectItemComponent implements OnInit {
  @Input()
  public project: Project;
  
  public selected: boolean;

  /**
 * 
 */
  public constructor(private service: ProjectService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.service.getSelectedProject().subscribe(this.updateSelection.bind(this));
  }

  /**
   * 
   */
  private updateSelection(project: Project): void {
    this.selected = project === this.project;
  }
  
  
  public onClick():void {
    this.service.setSelectedProject(this.project); 
  }
}
