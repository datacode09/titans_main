import {Envelope} from 'src/core/types/Envelope';
import {Objects} from 'src/core/types/Objects';

/**
 * 
 */
export class Geography {
  public readonly envelope: Envelope;

  public constructor(args: {
    envelope: Envelope
  }) {
    this.envelope = Objects.notNull(args.envelope, "Envelope cannot be null");
  }

  /**
   * 
   */
  public upperright(): any {
    const result = {x: this.envelope.xmax, y: this.envelope.ymax};
    return result;
  }

  /**
   * 
   */
  public lowerleft(): any {
    const result = {x: this.envelope.xmin, y: this.envelope.ymin};
    return result;
  }

  /**
   * 
   */
  public srid(): number {
    const result = this.envelope.spatialReference;
    return result;
  }

  /**
   * 
   */
  public toMapGeometry() {
    const result = {
      xmin: this.envelope.xmin,
      xmax: this.envelope.xmax,
      ymin: this.envelope.ymin,
      ymax: this.envelope.ymax,
      spatialReference: {wkid : this.envelope.spatialReference}
    };
    return result;
  }
}

