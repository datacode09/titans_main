import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Project} from './components/Project';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ProjectEntity} from './ProjectEntity';

/*
 * 
 */
@Injectable({
  providedIn: 'root'
})
export class ProjectSource {
  private baseUrl: string = "http://localhost:8081/titansdata.web";
  /**
   * 
   */
  public constructor(private http: HttpClient) {
  }

  /**
   * 
   */
  public loadProjects(): Observable<ProjectEntity[]> {
    const url = this.baseUrl + "/getProjects";
    const params = new HttpParams();
    const result = this.http.get(url, {params: params}).pipe<ProjectEntity[]>(map(this.responseToProjects.bind(this)));
    return result;
  }

  /**
   * 
   */
  private responseToProjects(response: any): ProjectEntity[] {
    const result = response.projects.map(this.toProjectEntity.bind(this));
    return result;
  }
    
  /**
   * 
   */
  private toProjectEntity(p:any):ProjectEntity {
    console.log(p); 
    const id: number = p.projectId;
      const title: string = p.projectName;
      const srid: number = p.srid;
      const lowerleft: any = p.lowerleft;
      const upperright: any = p.upperright;
      const rastergroupIds: any = p.rastergroupIds;
      const result = new ProjectEntity({
        projectId: id,
        name: title,
        lowerleft: lowerleft,
        upperright: upperright,
        srid: srid,
        rastergroupIds: rastergroupIds,
      });
      return result;
  }
}