import {Injectable} from '@angular/core';
import {Project} from './components/Project';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Geography} from './components/Geography';


@Injectable({
  providedIn: 'root'
})
export class ProjectStore {
  private baseUrl: string = "http://localhost:8081/titansdata.web.dev";

  public constructor(private http: HttpClient) {
  }

  /**
   * 
   */
  public storeProjects(projects: Project[]): void {
    projects.forEach(this.storeProjectIfNotExists.bind(this));
  }

  /**
   * 
   */
  private storeProjectIfNotExists(project: Project): void {
    this.ifProjectNotExists(project.id, () => this.storeProject(project));
  }

  /**
   * 
   */
  public storeSelectedProject(p: Project): void {
    const url = this.baseUrl + "/setProject";
    const params = new HttpParams()
      .set("project_id", p == null ? "" : p.id + "")
    this.http.post(url, {}, {params: params}).subscribe(response => {
      console.log(response);
    });
  }


  /**
   * 
   */
  private storeProject(p: Project): void {
    const url = this.baseUrl + "/createProject";
    const params = new HttpParams()
      .set("project_id", p.id + "")
      .set("project_name", p.title);
    this.http.post(url, {}, {params: params}).subscribe(response => {
      console.log(response);
    });
  }

  /**
   * 
   */
  private ifProjectNotExists(projectId: number, consumer: () => void): void {
    const url = this.baseUrl + "/projectExists";
    const params = new HttpParams()
      .set("project_id", projectId + "");
    this.http.get(url, {params: params}).subscribe((response: any) => {
      if (!response.exists) {
        consumer();
      }
    });
  }

  /**
   * 
   */
  public storeDataSources(projectId: number, a: number[]) {

  }

  /**
   * 
   */
  public storeGeography(projectId: number, g: Geography): void {
    const url = this.baseUrl + "/setProjectGeometry";
    const params = new HttpParams()
      .set("project_id", projectId + "")
      .set("lowerleft", g === null ? "" : JSON.stringify(g.lowerleft()))
      .set("upperright", g === null ? "" : JSON.stringify(g.upperright()))
      .set("srid", g === null ? "" : g.srid() + "");
    this.http.post(url, {}, {params: params}).subscribe((response: any) => {
      console.log(response);
    });
  }
}