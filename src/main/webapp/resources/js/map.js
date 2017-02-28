$(document).ready(function(){ 
jQuery('#vmap').vectorMap({
        map: 'europe_en',
        enableZoom: false,
        showTooltip: true,
        onRegionClick: function(element, code, region){	
        	//window.location.replace("../sights/country?country_code="+code);
        	
        	console.log("country code: " + code);
        	console.log("region: " + region);
        	console.log("element: " + element);
        	window.location.replace("country?country_code="+code);
        }
    });
});