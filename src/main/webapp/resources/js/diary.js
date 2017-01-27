$(document).ready(function(){
	$("#add-book-btn").click(function(){
        $("#modal-form").modal();
    });
	
    $("#update-book-btn").click(function() {   	   	
    	var id = $("#crud-tbl tr.danger").find('td:first').html();
    	console.log(id); 
    	$.get( "books/edit/"+id, function(data) {
    		event.preventDefault();
    		$("#id").val(id);
    		$("#title").val(data.book_title);
    		$("#description").val(data.book_description);
    		$("#author").val(data.author);
        	$("#modal-form").modal();
    	});
    });
    
    $("#delete-book-btn").click(function() {
    	$("#modal-dialog").modal();
    });
    
    $("#modal-dialog-ok-btn").click(function() {
    	event.preventDefault();
    	var id = $("#crud-tbl tr.danger").find('td:first').html();
    	console.log (id); 
    	$.get( "books/remove/"+id, function(data) {
    		console.log(data);
    		$("#crud-tbl tr:contains('" + id + "')").remove();  	  
    	})
    	  .done(function() {
		    console.log("done");
		  })
		  .fail(function(e) {
		    console.log( "error" + e );
		  })

    	$("#modal-dialog").modal("hide");
    });
    $("#modal-dialog-cancel-btn").click(function() {
    	$("#modal-dialog").modal("hide");
    });
    
    $("#modal-form-cancel-btn").click(function() {
    	event.preventDefault();
    	hideModalDialog();
    });
    $("#modal-form-save-book-btn").click(function(){
    	event.preventDefault();
    	
    	var id = $("#id");
    	var title = $("#title");
    	var description = $("#description");
    	var author = $("#author");
    	
    	var data = {}
    	data["book_id"] = id.val();
    	data["book_title"] = title.val();
        data["book_description"] = description.val();
        data["author"] = author.val();
        
        tips = $( ".validateTips" );
        var valid = true;
        valid = valid && checkLength( title, "title", 3, 16 );
        valid = valid && checkLength( description, "description", 3, 80 );
        valid = valid && checkLength( author, "author", 3, 80 );
      
        if(valid) {
	        $.ajax({
	     	   url:"add-book",
	     	   type:"POST",
	     	   data: JSON.stringify(data),
	     	   contentType:"application/json; charset=utf-8",
	     	   dataType:"json",
	     	   success: function(obj){
	     		    hideModalDialog();
	     		   if(id != -1) {   
	     		        $("#crud-tbl tr:contains('" + obj.book_id + "')").remove();  		    
	     		    }
	     		    $("#crud-tbl tbody").append("<tr class='clickable-row'><td>"+obj.book_id+"</td><td>"+obj.book_title+"</td><td>"+obj.author+"</td></tr>");  		  		
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
    $('#crud-tbl').on('click', '.clickable-row', function(event) {
    	  $(this).addClass('danger').siblings().removeClass('danger');
    	  //var valur =$(this).find('td:first').html();
    	   //alert(value);    
    });	 
   $('table').on('click', '.row_action', function(event) {
	   event.preventDefault();
	   var id = $("#crud-tbl tr.danger").find('td:first').html();
	   console.log(id);
	   window.location.replace("sight_posts?sight_id="+id);
   });
});

//End of document.ready
function hideModalDialog() {
	$("#modal-form").modal("hide");
	$(':input','#modal-form')
    .not(':button, :submit, :reset, :hidden')
    .val('')
    .removeAttr('checked')
    .removeAttr('selected');
	$(':input[type=hidden]','#modal-form').val('-1');
}
function updateTips( t ) {
    tips
      .text( t )
      .addClass( "alert alert-danger" );
    setTimeout(function() {
      tips.removeClass( "alert alert-danger", 1500 );
    }, 500 );
  }
function checkLength( o, n, min, max ) {
    if ( o.val().length > max || o.val().length < min ) {
      o.addClass( "alert alert-danger" );
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
/*function loadTable(table_id, data_url) {
	var myTable = $('#crud-tbl > tbody');
	var jqresp = $.get( "list-posts", function(data) {
		$.each(data, function(i, obj) {
			//alert(obj.title);
		    myTable.append("<tr><td>"+obj.post_id+"</td><td>"+obj.title+"</td><td>"+obj.text+"</td></tr>");                                
		});
	});
}*/

