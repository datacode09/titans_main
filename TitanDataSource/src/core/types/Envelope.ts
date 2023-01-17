

export class Envelope {
  public xmin: number;
  public ymin: number;
  public xmax: number;
  public ymax: number;
  public spatialReference: number;

  constructor(xmin: number, xmax: number, ymin: number, ymax: number, spatialReference: number) {
    this.xmin = xmin;
    this.ymin = ymin;
    this.xmax = xmax;
    this.ymax = ymax;
    this.spatialReference = spatialReference;
  }
}