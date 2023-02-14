import {Injectable} from '@angular/core';
import {Observable, BehaviorSubject} from 'rxjs';

import {Geography} from './components/Geography';
import {DataSource} from './components/DataSource';
import {Project} from './components/Project';
import {ProjectStore} from './ProjectStore';
import {ProjectSource} from './ProjectSource';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private projects: BehaviorSubject<Project[]> = new BehaviorSubject([]);
  private geographies: Map<number, BehaviorSubject<Geography>> = new Map();
  private projectdata: Map<number, BehaviorSubject<DataSource[]>> = new Map();
  private selectedProject: BehaviorSubject<Project> = new BehaviorSubject(null);
  
  /**
   * 
   */
  public constructor(private source:ProjectSource, private store:ProjectStore) {
    this.projects.subscribe(this.store.storeProjects.bind(this.store));
    this.projects.subscribe(this.initGeographies.bind(this));
    this.projects.subscribe(this.initProjectDataSource.bind(this));
    setTimeout(()=>{
      this.source.loadProjects().subscribe(projEntities=>{
        const projects = projEntities.map(p=>p.toProject());
        this.projects.next(projects);
      }); 
    });
    
  }

  /**
   * 
   */
  private initGeographies(projects: Project[]): void {
    projects.forEach(p => {
      if (!this.geographies.has(p.id)) {
        this.geographies.set(p.id, new BehaviorSubject(null));
      }
    });
  }

  /**
   * 
   */
  private initProjectDataSource(projects: Project[]): void {
    projects.forEach(p => {
      if (!this.projectdata.has(p.id)) {
        const arr:DataSource[] = [];
        for (let i = 1; i <= Math.round(Math.random()*10); i++) {
          const d = new DataSource("Fake data 01 for " + p.title);
          arr.push(d); 
        }
        this.projectdata.set(p.id, new BehaviorSubject(arr));
      }
    });
  }

  /*
   * 
   */
  public getProjects(): Observable<Project[]> {
    return this.projects;
  }

  /**
   * 
   */
  public getProjectGeography(projectId: number): Observable<Geography> {
    return this.geographies.get(projectId);
  }


  /**
   * 
   */
  public getProjectData(projectId: number): Observable<DataSource[]> {
    return this.projectdata.get(projectId);
  }

  /**
   * 
   */
  public createProject(): void {
    const project = Project.createInstance();
    const newprojects = this.projects.value.slice();
    newprojects.push(project)
    this.projects.next(newprojects);
    this.setSelectedProject(project);
  }

  /**
   * 
   */
  public setSelectedProject(project: Project): void {
    this.selectedProject.next(project);
  }


  /**
   * 
   */
  public setGeography(projectId: number, geography: Geography): void {
    this.geographies.get(projectId).next(geography);
  }


  /**
   * 
   */
  public setProjectData(projectId: number, datasources: DataSource[]): void {
    this.projectdata.get(projectId).next(datasources);
  }

  /**
   * 
   */
  public getSelectedProject(): Observable<Project> {
    return this.selectedProject;
  }

  /**
   * 
   */
  public getSelectedProjectValue(): Project {
    return this.selectedProject.value;
  }
}

