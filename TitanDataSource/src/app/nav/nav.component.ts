import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

/**
 * 
 */
@Component({
  selector: 'nav-root',
  templateUrl: './nav.component.html',
  styleUrls: ['../app.component.css']
  
})
export class NavComponent implements OnInit {
  public navlinks:any[] = [
    {name: "Home", path: "main"}
    , {name: "Datasets", path: "datasets"}
    , {name: "Models", path: ""}
    , {name: "Export API", path: "export-api"}
  ];
  
  public constructor(private router:Router) {
  }
  /**
   * 
   */
  public ngOnInit(): void {
    this.router.events.subscribe(this.updateNavLinksActiveState.bind(this)); 
  }
  
  /**
   * 
   */
  private updateNavLinksActiveState() {
    this.navlinks.forEach(navlink=>{
        $("#routernav-" + navlink.path).removeClass("active"); 
      }); 
      const navlink = this.navlinks.find(n => n.path !== "" && "/" + n.path === this.router.url); 
      if (navlink !== undefined) {
        $("#routernav-" + navlink.path).addClass("active"); 
      }
  }

}

