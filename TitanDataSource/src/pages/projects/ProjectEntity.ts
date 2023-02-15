import {Project} from './components/Project';
import {Geography} from './components/Geography';
import {Envelope} from 'src/core/types/Envelope';
import {Objects} from 'src/core/types/Objects';


export class ProjectEntity {
  public readonly projectId: number;
  public readonly name: string;
  public readonly lowerleft: any;
  public readonly upperright: any;
  public readonly srid: number;
  public readonly rastergroupIds: number[];

  public constructor(arg: {
    projectId: number,
    name: string,
    lowerleft: any,
    upperright: any,
    srid: number,
    rastergroupIds: number[]
  }) {
    this.projectId = arg.projectId;
    this.name = arg.name;
    this.lowerleft = arg.lowerleft;
    this.upperright = arg.upperright;
    this.srid = arg.srid;
    this.rastergroupIds = arg.rastergroupIds;
  }

  /**
   * 
   */
  public toGeometry(): Geography {
    let result: Geography = null;
    if (Objects.isNotNull(this.srid)) {
      const xmin = this.lowerleft.x;
      const xmax = this.upperright.x;
      const ymin = this.lowerleft.y;
      const ymax = this.upperright.y;
      const spatialReference = this.srid;
      const envelope = new Envelope(xmin, xmax, ymin, ymax, spatialReference);
      result = new Geography({envelope:envelope});
    } 
    return result;
  }

  /**
   * 
   */
  public toProject(): Project {
    const result = new Project(this.projectId, this.name);
    return result;
  }
}
