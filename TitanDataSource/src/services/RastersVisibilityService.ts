import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http/http';
import {Router} from '@angular/router';
import {BehaviorSubject, Subject} from 'rxjs';

/**
 * 
 */
@Injectable({
  providedIn: 'root'
  , deps: [HttpClient, Router]
})
export class RastersVisibilityService {

  private map: Map<number, BehaviorSubject<boolean>> = new Map();

  /**
   * 
   */
  public constructor() {
  }

  /**
   * 
   */
  public getVisibility(rasterId: number): BehaviorSubject<boolean> {
    if (!this.map.has(rasterId)) {
      const instance: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
      this.map.set(rasterId, instance);
    }
    return this.map.get(rasterId);
  }
  
}

