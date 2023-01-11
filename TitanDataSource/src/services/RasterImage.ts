import {Envelope} from './Envelope';

export class RasterImage {
  
  public imageURL:string;
  public envelope:Envelope;
  
  constructor(imageURL: string, envelope: Envelope) {
    this.imageURL = imageURL;
    this.envelope = envelope;
  }
}