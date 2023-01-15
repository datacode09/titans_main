/* global Objects */

function onEsriLoad(callback) {
  const a  = setInterval(()=>{
    if (window["require"] !== undefined) {
      console.log("'require' does exist")
      clearInterval(a); 
      require(['esri', //
        'esri/map', //
        'dojo/domReady', //
        'esri/dijit/BasemapLayer', //
        'esri/dijit/Basemap']);
      callback(); 
    }
  }, 1000);
}




