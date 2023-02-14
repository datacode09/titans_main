import {OnInit, Component} from '@angular/core';
import {ProjectService} from '../../ProjectService';
import {Project} from '../Project';


/**
 * 
 */
@Component({
  selector: "projectslist-root"
  , templateUrl: "./projectslist.component.html"
  , styleUrls: ["./projectslist.component.css"]
})
export class ProjectsListComponent implements OnInit {
  
  public projects:Project[] = [];
  
  /**
   * 
   */
  public constructor(private service:ProjectService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.service.getProjects().subscribe(this.updateProjects.bind(this)); 
    
  }
  
  
  
  /**
   * 
   */
  private updateProjects(projects: Project[]):void {
    this.projects = projects;
  }
  
  /**
   * 
   */
  public onNewProject(): void {
    this.service.createProject();
  }
}