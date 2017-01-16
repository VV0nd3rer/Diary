$(document).ready(function () {
		    var dialog, form,
		    dialog = $( "#dialog-form" ).dialog({
		      autoOpen: false,
		      height: 400,
		      width: 350,
		      modal: true,
		      buttons: {
		        "Create": addRow,
		        Cancel: function() {
		          dialog.dialog( "close" );
		        }
		      },
		      close: function() {
		        form[ 0 ].reset();
		        allFields.removeClass( "ui-state-error" );
		        
		      }
		    });
		 
		    form = dialog.find( "form" ).on( "submit", function( event ) {
		      event.preventDefault();
		      addRow();
		    });
		 
		    $( "#add-update-button" ).button().on( "click", function() {
		      dialog.dialog( "open" );
		    });
		
		  //send as array allFields = $( [] ).add( title ).add( description ).add( text )
		    function addRow() {
		    	 //emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
		         title = $( "#title" ),
		         description = $( "#description" ),
		         text = $( "#text" ),
		         allFields = $( [] ).add( title ).add( description ).add( text ),
		         tips = $( ".validateTips" );
		         
		         var valid = true;
		         allFields.removeClass( "ui-state-error" );

		         valid = valid && checkLength( title, "title", 3, 16 );
		         valid = valid && checkLength( description, "description", 3, 80 );
		         valid = valid && checkLength( text, "text", 3, 80 );
		         
		        /* valid = valid && checkRegexp( name, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter." );
		         valid = valid && checkRegexp( email, emailRegex, "eg. ui@jquery.com" );
		         valid = valid && checkRegexp( password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9" );*/
		         //Client side vs server side validation
		         //Go to controller with json data - $.get
		         if ( valid ) {
		        	 
		           /*$( "#crudTable tbody" ).append( "<tr>" +
		             "<td>" + title.val()+ "</td>" +
		             "<td>" + description.val() + "</td>" +
		             "<td>" +  text.val() + "</td>" +
		           "</tr>" );*/
		           var data = {}
		       	   data["title"] = title.val();
		           data["description"] = description.val();
		           data["text"] = text.val();
		           $.ajax({
		        	   url:"add-post",
		        	   type:"POST",
		        	   data: JSON.stringify(data),
		        	   contentType:"application/json; charset=utf-8",
		        	   dataType:"json",
		        	   success: function(obj){
		        	     console.log("OK");
		        	     $("#crudTable tbody").append("<tr><td>"+obj.post_id+"</td><td>"+obj.title+"</td><td>"+obj.text+"</td></tr>");   
		        	   },
		        	   error : function(e) {
		       			console.log("ERROR: ", e);
		       			display(e);
		    	   		},
		    	   		done : function(e) {
		    	   			console.log("DONE");
		    	   		}
		        	 });
		         
		           dialog.dialog( "close" );
		         }
		         
		         $(':input','#dialog-form')
		         .not(':button, :submit, :reset, :hidden')
		         .val('')
		         .removeAttr('checked')
		         .removeAttr('selected');
		         
		         
		         return valid;
		    }
});

//Thanks to jQueryUI examples
function updateTips( t ) {
    tips
      .text( t )
      .addClass( "ui-state-highlight" );
    setTimeout(function() {
      tips.removeClass( "ui-state-highlight", 1500 );
    }, 500 );
  }

  function checkLength( o, n, min, max ) {
    if ( o.val().length > max || o.val().length < min ) {
      o.addClass( "ui-state-error" );
      updateTips( "Length of " + n + " must be between " +
        min + " and " + max + "." );
      return false;
    } else {
      return true;
    }
  }

  function checkRegexp( o, regexp, n ) {
    if ( !( regexp.test( o.val() ) ) ) {
      o.addClass( "ui-state-error" );
      updateTips( n );
      return false;
    } else {
      return true;
    }
  }
  //
function loadTable(table_id, data_url) {
	var myTable = $('#crudTable > tbody');
	var jqresp = $.get( "list-posts", function(data) {
		$.each(data, function(i, obj) {
			//alert(obj.title);
		    myTable.append("<tr><td>"+obj.post_id+"</td><td>"+obj.title+"</td><td>"+obj.text+"</td></tr>");                                
		});
		//alert( "Data Loaded: " + data.title );
	});
}

