import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';


/**
 * 
 */
@Component({
  selector: 'nav-root',
  templateUrl: './nav.component.html',
  styleUrls: ['../../app/app.component.css']

})
export class NavComponent implements OnInit {
  public navlinks: any[] = [
    {name: "Home", path: "main"}
    , {name: "Projects", path: "projects"}
    , {name: "Data Sources", path: "datasets"}
    , {name: "Models", path: "models"}
    , {name: "Export API", path: "export-api"}
    
  ];

  public constructor(private router: Router, private location:Location) {
  }
  /**
   * 
   */
  public ngOnInit(): void {
    this.router.events.subscribe(this.updateNavLinksActiveState.bind(this));
    setTimeout(()=>{  
      this.updateNavLinksActiveState();  
    });
  }

  /**
   * 
   */
  private updateNavLinksActiveState() {
    this.navlinks.forEach(this.removeNavActiveState.bind(this));
    const currUrl = this.getCurrentUrl();
    const navlink = this.findActiveNavLink(currUrl);
    this.setNavLinkActiveState(navlink);
  }
  
  /**
   * 
   */
  private getCurrentUrl():string {
    const result = this.location.path().replace("/home", "");
    return result;
  }
  
  /**
   * 
   */
  private findActiveNavLink(currUrl:string):any {
    const navlink = this.navlinks.find(n => n.path !== "" && "/" + n.path === currUrl);
    return navlink;
  }
  
  /**
   * 
   */
  private removeNavActiveState(navlink: any) {
    this.getRouterNavLink(navlink).removeClass("active");
  }
    
  /**
   * 
   */
  private setNavLinkActiveState(navlink:any):void {
    if (navlink !== undefined) {
      this.getRouterNavLink(navlink).addClass("active");
    }
  }
  
  /**
   * 
   */
  private getRouterNavLink(navlink:any):any {
    return $("#routernav-" + navlink.path);
  }
}

