import {RouteReuseStrategy} from '@angular/router';
import {ActivatedRouteSnapshot, DetachedRouteHandle} from '@angular/router';

export class CacheRouteReuseStrategy implements RouteReuseStrategy {
  private storedRouteHandles = new Map<string, DetachedRouteHandle>();

  private allowRetrieveCache: any = {
    'main': true, 
    'projects': true
  };

  /**
   * Overriding method
   */
  public shouldDetach(route: ActivatedRouteSnapshot): boolean {
    const path = this.getPath(route);
    if (this.allowRetrieveCache.hasOwnProperty(path)) {
      return true;
    }
    return false;
  }

  /**
   * Overriding method
   */
  public store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
    this.storedRouteHandles.set(this.getPath(route), handle);
  }

  /**
   * Overriding method
   */
  public shouldAttach(route: ActivatedRouteSnapshot): boolean {
    const path = this.getPath(route);
    if (this.allowRetrieveCache[path]) {
      return this.storedRouteHandles.has(this.getPath(route));
    }
    return false;
  }
  /**
   * Overriding method
   */
  public retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
    return this.storedRouteHandles.get(this.getPath(route)) as DetachedRouteHandle;
  }
  
  /**
   * Overriding method
   */
  public shouldReuseRoute(before: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
    const beforepath = this.getPath(before);
    const currpath = this.getPath(curr);
    if (beforepath === 'login') {
      Object.keys(this.allowRetrieveCache).forEach(k=>{        
        this.allowRetrieveCache[k] = false;
      });
    } else {
      Object.keys(this.allowRetrieveCache).forEach(k=>{        
        this.allowRetrieveCache[k] = true;
      });
    }
    return before.routeConfig === curr.routeConfig;
  }
  
  /**
   * 
   */
  private getPath(route: ActivatedRouteSnapshot): string {
    if (route.routeConfig !== null && route.routeConfig.path !== null) {
      return route.routeConfig.path;
    }
    return '';
  }


}

