import {Injectable} from '@angular/core';
import {Observable, BehaviorSubject} from 'rxjs';

import {Geography} from './components/Geography';
import {DataSource} from './components/DataSource';
import {Project} from './components/Project';
import {ProjectStore} from './ProjectStore';
import {ProjectSource} from './ProjectSource';
import {RastersService} from 'src/services/rasterservices/RastersService';

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
  public constructor(private service:RastersService, private source: ProjectSource, private store: ProjectStore) {
    this.projects.subscribe(this.store.storeProjects.bind(this.store));
    this.projects.subscribe(this.initGeographies.bind(this));
    this.projects.subscribe(this.initProjectDataSource.bind(this));
    this.selectedProject.subscribe(this.store.storeSelectedProject.bind(this.store));
    setTimeout(() => {
      this.source.loadProjects().subscribe(projEntities => {
        const projects = projEntities.map(p => p.toProject());
        this.projects.next(projects);
        projEntities.forEach(p => {
          this.setGeography(p.projectId, p.toGeometry());
        });
      });
    });
  }
  

  /**
   * 
   */
  private initGeographies(projects: Project[]): void {
    projects.forEach(p => {
      if (!this.geographies.has(p.id)) {
        const sub = new BehaviorSubject(null);
        this.geographies.set(p.id, sub);
        sub.subscribe(g => this.storeGeography(p.id, g));
      }
    });
  }

  /**
   * 
   */
  private storeGeography(projectId: number, g: Geography): void {
    if (g != null) {
      this.store.storeGeography(projectId, g);
    }
  }

  /**
   * 
   */
  private storeDataSources(projectId: number, a: DataSource[]) {
    this.store.storeDataSources(projectId, a.map(d => d.rastergroupid));
  }


  /**
   * 
   */
  private initProjectDataSource(projects: Project[]): void {
    projects.forEach(p => {
      if (!this.projectdata.has(p.id)) {
        const sub = new BehaviorSubject([]);
        this.service.getRasters().subscribe(arr=>{
          sub.next(arr.map(a => new DataSource(a.id, a.name)));
        }); 
        this.projectdata.set(p.id, sub);
        sub.subscribe(a => this.storeDataSources(p.id, a));
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

