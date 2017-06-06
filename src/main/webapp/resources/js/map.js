$(document).ready(function(){ 
/*jQuery('#vmap').vectorMap({
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
    });*/	
var isNewSight = false;

var map = new GMaps({
	  div: '#map',
	  lat: 49.817492,
	  lng: 15.472962,
	  zoom: 7
});
var infowindow = new google.maps.InfoWindow();

$.get("get-sights", function(data) {
	for (var i in data) {
		var sight_id = data[i].sight_id;
		console.log('sight ID ' + sight_id);
		map.addMarker({
			  lat: data[i].map_coord_x,
			  lng: data[i].map_coord_y,
			  title: data[i].sight_label,
			  description: data[i].sight_description,
			 /* infoWindow: {
				  content: '<p><a href="./posts/sight/' + data[i].sight_id + '">See more</a> or <button id="update-sight-btn" value="' + data[i].sight_id + '" type="submit"/>Update sight</button></p>'
			  },*/
			  id: data[i].sight_id,
			  click: function(e) {
				  infowindow.setContent('<p>' + e.title + '</p><p>' + e.description + '</p><a href="../posts/sight/' + e.id + '">See more</a> or <button onclick="updSight(' + e.id + ')" type="submit"/>Update sight</button>');
				  infowindow.open(map, this);
				  /*var r = confirm("Do you want to see more about this place?");
				    if (r == true) {
				        txt = "You pressed OK!";
				        window.location.href="posts/" + e.id;
				        //posts/data[i].sight_id
				    } else {
				        txt = "You pressed Cancel!";
				    }*/
			  }
			});

	}
	//console.log(data.map_coord_x);
})
.done(function() {
	console.log("done");
})
.fail(function(e) {
	console.log( "error" + e );
})

$('#geocoding_form').submit(function(e){
    e.preventDefault();
    GMaps.geocode({
      address: $('#address').val().trim(),
      callback: function(results, status){
        if(status=='OK'){
        	var latlng = results[0].geometry.location;
        	var country_code;
        	var country_name;
        	var label = $('#address').val().trim();
	        raw = results;
	        //find country name
	        for (var i=0; i < results[0].address_components.length; i++) {
	         for (var j=0; j < results[0].address_components[i].types.length; j++) {
	          if (results[0].address_components[i].types[j] == "country") {
	            country = results[0].address_components[i];
	            country_code = country.short_name;
	            country_name = country.long_name;
	          }
	         }
	        }
        	
          
          map.setCenter(latlng.lat(), latlng.lng());
          $.get("is-coord-stored", { x: latlng.lat(), y: latlng.lng() }, function(res) {
        	 if(res == true) {
        		 isNewSight = false;
        		 return;
        	 }
        		 isNewSight = true;
                 map.addMarker({
                   lat: latlng.lat(),
                   lng: latlng.lng(),
                   title: $('#address').val(),
                   click: function(e) {
                   	 if(isNewSight) {
       				  var r = confirm("Do you want to save this place?");
       				    if (r == true) {
       				       $("#lat").val(latlng.lat());
       				       $("#lng").val(latlng.lng());
       				       $("#country_code").val(country_code);
       				       $("#country_name").val(country_name);
   				    	   $("#label").val(label);
       				       $("#modal-form").modal();
       				       txt = "OK";
       				       isNewSight = false;
       				    } else {
       				        txt = "You pressed Cancel!";
       				    }
                   	 }
                   	 else {
                   		 infowindow.setContent('<p>' + e.title + '</p>');
          				 infowindow.open(map, this);
                   	 }
       			  }
                 });
          });
        }
      }
    });
    
    
});

//$("#update-sight-btn").click(function() {
	/*var id = $(this).val();
	alert(id);
	var upd_sight_url = "../sights/edit/"+id;
	$.get(upd_sight_url, function(data) {
		//event.preventDefault();
		$("#sight_id").val(id);
		$("#code").val(data.country_code);
		$("#label").val(data.sight_label);
		$("#description").val(data.sight_description);
    	$("#modal-form").modal();
	});*/
//});

$("#modal-form-save-sight-btn").click(function(event) {
	event.preventDefault();

	var id = $("#sight_id");
	var code = $("#country_code");
	var country_name = $("#country_name");
	var label = $("#label");
	var description = $("#description");
	var lat = $("#lat");
	var lng = $("#lng");
	var data = {}
	var country = {}
	country["country_code"] = code.val();
	country["country_name"] = country_name.val();
	data["sight_id"] = id.val();
	
	data["country"] = country;
	data["sight_label"] = label.val();
	data["sight_description"] = description.val();
	data["map_coord_x"] = lat.val();
	data["map_coord_y"] = lng.val();
	   
    tips = $( ".validateTips" );
    var valid = false;
    valid = checkLength( code, "code", 2, 2 );
    valid = valid && checkLength( label, "label", 3, 80 );
    valid = valid && checkLength( description, "description", 3, 80 );
    
    var token = $("meta[name='_csrf']").attr("content");
	   var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
	        xhr.setRequestHeader(header, token);
	    });
    
    var save_sight_url = "../sights/add-sight";
    if(valid) {
    	$.ajax({
	     	   url: save_sight_url,
	     	   type:"POST",
	     	   data: JSON.stringify(data),
	     	   contentType:"application/json; charset=utf-8",
	     	   dataType:"json",
	     	   success: function(obj){
	     		   hideModalDialog();
	     		   if(id == 0) {   
	     			 //new sight was added
	     			 
	     		      
	     		    }
	     		    
	     	   },
	     	   error : function(e) {
	     		    console.log("Error: ", e);
	    			display(e);
	 	   		},
	 	   		done : function(e) {
	 	   			alert("DONE");
	 	   		}
	     	 });
    }
    
});
});
function updSight(id) { 
	alert ('ok, ' + id); 
	var upd_sight_url = "../sights/edit/"+id;
	$.get(upd_sight_url, function(data) {
		//event.preventDefault();
		$("#sight_id").val(id);
		$("#code").val(data.country_code);
		$("#label").val(data.sight_label);
		$("#description").val(data.sight_description);
		$("#lat").val(data.map_coord_x);
		$("#lng").val(data.map_coord_y);
    	$("#modal-form").modal();
	});	
}