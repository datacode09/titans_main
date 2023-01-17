import {Envelope} from '../types/Envelope';


export class RasterImage {
  
  public imageURL:string;
  public envelope:Envelope;
  
  public constructor(imageURL: string, envelope: Envelope) {
    this.imageURL = imageURL;
    this.envelope = envelope;
  }
}