import {Objects} from './Objects';


export class Envelope {
  public xmin: number;
  public ymin: number;
  public xmax: number;
  public ymax: number;
  public spatialReference: number;

  constructor(xmin: number, xmax: number, ymin: number, ymax: number, spatialReference: number) {
    this.xmin = Objects.notNull(xmin, "xmin cannot be null");
    this.ymin = Objects.notNull(ymin, "ymin cannot be null");
    this.xmax = Objects.notNull(xmax, "xmax cannot be null");
    this.ymax = Objects.notNull(ymax, "ymax cannot be null");
    this.spatialReference = Objects.notNull(spatialReference, "spatialReference cannot be null");
  }
}