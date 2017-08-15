$(document).ready(function(){
	var root = '';

	$("#create-post-ok-btn").click(function(event) {
		event.preventDefault();
		var description = tinyMCE.get('descrEditor').getContent();
		var postText = tinyMCE.get('textEditor').getContent();
		var id = $("#id");
		//var sight_id = $("#sight").find(":selected");
		var title = $("#title");
		//var description = $("#description");
		console.log("id: " + id.val());
		//console.log("sight id: " + sight_id.val());
		var data = {}
		var sight = {}
		//data["sight_id"] = sight_id.val();
		data["post_id"] = id.val();
		data["title"] = title.val();
		data["description"] = description;//description.val();
		data["text"] = postText;
		
		tips = $( ".validateTips" );
		
		var valid = false;
	    valid = checkMinMaxLength(title, "title", 2, 80);
		//valid = valid && checkMinMaxLength(description, "description", 2, 80);
		valid = valid && checkTextEditorMinMaxLength(description, $("#descrEditor"), "text", 5, 1000);
		valid = valid && checkTextEditorMinLength(postText, $("#textEditor"), "text", 10);

		var token = $("meta[name='_csrf']").attr("content");
	 	var header = $("meta[name='_csrf_header']").attr("content");
	 	$(document).ajaxSend(function(e, xhr, options) {
	        xhr.setRequestHeader(header, token);
	    });
	 	if(valid) {
	 		var add_post_url = root + "/posts/save-post";
	 		var posts_list_url =  root + "/posts/list";
	 		$.ajax({
		     	   url: add_post_url,
		     	   type:"POST",
		     	   data: JSON.stringify(data),
		     	   contentType:"application/json; charset=utf-8",
		     	   dataType:"json",
		     	   success: function(res){
		     		  console.log(res.respCode);
					  console.log(res.respMsg);
					  if(res.respCode == 'OK') {
						  window.location.href = posts_list_url;
					  }
					  else /* if(res.respCode == 'PRECONDITION_FAILED') */{
						   addErrMsg(res.respMsg);
					  }
		     	   },
		     	   error : function(e) {
		    			window.location.href = posts_list_url;
		 	   		},
		 	   		done : function(e) {
		 	   			alert("DONE");
		 	   		}
		     	 });
	 	}
	 	
	});
	
	$("#add-comment").click(function(event) {
		event.preventDefault();
		var post_id = $("#post-id");
		console.log(post_id.val());
		var text = $("#text");
		var comment = new Object();
		comment.text = text.val();
		comment.post_id = post_id.val();
	
		tips = $( ".validateTips" );
	    var valid = false;
	    valid = checkMinMaxLength(text, "text", 2, 80);
	    var token = $("meta[name='_csrf']").attr("content");
 	    var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
	        xhr.setRequestHeader(header, token);
	    });
        
        if(valid) {
        	var add_comment_url = root + "/posts/add-comment";
	        $.ajax({
	     	   url: add_comment_url,
	     	   type:"POST",
	     	   data: JSON.stringify(comment/*data*/),
	     	   contentType:"application/json; charset=utf-8",
	     	   dataType:"json",
	     	   success: function(res){
	     		   console.log(res.respCode);
	     		   console.log(res.respMsg);
	     		  if(res.respCode == 'OK') {  
	     			   var comment_block = '<div class="row"><div class="col-sm-1"><div class="thumbnail"><img class="img-responsive user-photo" src="https://ssl.gstatic.com/accounts/ui/avatar_2x.png"></div></div><div class="col-sm-5"><div class="panel panel-default"><div class="panel-heading"><strong>' + res.responseObject.user.username + '</strong><p> commented <span class="text-muted">just now</span></p></div><div class="panel-body">' + res.responseObject.text + '</div></div></div></div>'
					   console.log("comment date: " + res.responseObject.comment_datetime);
	     			   $("#comment-section").append(comment_block);
					   $("#text").val('');
				   }
				   else /*if(res.respCode == 'PRECONDITION_FAILED')*/ {
					   addErrMsg(res.respMsg);
				   } 		  		
	     	   },
	     	   error : function(e) {
	     		    console.log("Error: ", e);
	    			//display(e);
	 	   		},
	 	   		done : function(e) {
	 	   			alert("DONE");
	 	   		}
	     	 });
        }
	});
	
	//Books page CRUD functionality
	$("#add-book-btn").click(function(){
        $("#modal-form").modal();
    });
	$("#add-sight-btn").click(function() {
		$("#modal-form").modal();
	});
    $("#update-book-btn").click(function() {
    	var id = $("#crud-tbl tr.danger").find('td:first').html();
    	console.log(id);
    	var upd_book_url = root + "/books/edit/"+id;
    	$.get(upd_book_url, function(data) {
    		//event.preventDefault();
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
    $("#delete-sight-btn").click(function() {
    	$("#modal-dialog").modal();
    });
    $("#modal-dialog-ok-btn").click(function(event) {
    	event.preventDefault();
    	var id = $("#crud-tbl tr.danger").find('td:first').html();
    	console.log (id); 
    	var rm_book_url = root+"/books/remove/"+id;
    	$.get(rm_book_url, function(data) {
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
    $("#modal-dialog-ok-rm-sight-btn").click(function() {
    	event.preventDefault();
    	var id = $("#crud-tbl tr.danger").find('td:first').html();
    	console.log (id); 
    	var rm_sight_url = root+"/sights/remove/"+id;
    	$.get(rm_sight_url, function(data) {
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
    
    $("#modal-form-cancel-btn").click(function(event) {
    	event.preventDefault();
    	hideModalDialog();
    });
    $("#modal-form-save-book-btn").click(function(event){
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
        valid = valid && checkMinMaxLength( title, "title", 3, 30 );
        valid = valid && checkMinMaxLength( description, "description", 3, 80 );
        valid = valid && checkMinMaxLength( author, "author", 3, 80 );
        
        var token = $("meta[name='_csrf']").attr("content");
 	    var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
	        xhr.setRequestHeader(header, token);
	    });
        
        var save_book_url = root+"/books/add-book";
        if(valid) {
        	$.ajax({
	     	   url: save_book_url,
	     	   type:"POST",
	     	   data: JSON.stringify(data),
	     	   contentType:"application/json; charset=utf-8",
	     	   dataType:"json",
	     	   success: function(obj){
	     		   hideModalDialog();
	     		   if(id != 0) {   
	     		        $("#crud-tbl tr:contains('" + obj.book_id + "')").remove();  		    
	     		    }
	     		    $("#crud-tbl tbody").append("<tr class='clickable-row'><td>"+obj.book_id+"</td><td>"+obj.book_title+"</td><td>"+obj.author+"</td></tr>");  		  		
	     	   },
	     	   error : function(e) {
	     		    console.log("Error: ", e);
	 	   		},
	 	   		done : function(e) {
	 	   			alert("DONE");
	 	   		}
	     	 });
        }
        
    });

 
    $('#crud-tbl').on('click', '.clickable-row', function(event) {
    	  console.log('click on crud-tbl class');
		  $('#update-book-btn').removeAttr('disabled');
		  $('#delete-book-btn').removeAttr('disabled');
    	  $(this).addClass('danger').siblings().removeClass('danger');
    	  //var valur =$(this).find('td:first').html();
    	   //alert(value);    
    });	 
   $('table').on('click', '.row_action', function(event) {
	   event.preventDefault();
	   var id = $("#crud-tbl tr.danger").find('td:first').html();
	   console.log(id);
	   window.location.replace("posts/sight_posts?sight_id="+id);
   });
   //--- End of Books page CRUD functionality	

});
//End of document.ready
function hideModalDialog() {
	$("#modal-form").modal("hide");
	$(':input','#modal-form')
    .not(':button, :submit, :reset, :hidden')
    .val('')
    .removeAttr('checked')
    .removeAttr('selected');
	$(':input[type=hidden]','#modal-form').val('0');
}
function addErrMsg( t ) {
    tips
      .text( t )
      .addClass( "alert alert-danger" );
    /*setTimeout(function() {
        tips.removeClass( "alert alert-danger", 1500 );
    	tips.empty();
    }, 500 );*/
}
function remErrMsg() {
	tips.removeClass( "alert alert-danger", 1500 );
	tips.empty();
}

function checkMinMaxLength(o, n, min, max ) {
    if ( o.val().length > max || o.val().length < min /*|| o.length > max || o.length < min*/) {
      o.addClass( "alert alert-danger" );
      addErrMsg( "Length of " + n + " must be between " +
        min + " and " + max + "." );
      return false;
    } else {
      remErrMsg();
      return true;
    }
  }
function checkTextEditorMinLength(content, field, field_title, min) {
	if (content.length < min) {
		field.addClass( "alert alert-danger" );
		addErrMsg( "Length of " + field_title + " must be mere than " +
			min + "." );
		return false;
	} else {
		remErrMsg();
		return true;
	}
}
function checkTextEditorMinMaxLength(content, field, field_title, min, max ) {
	if (content.length > max || content.length < min) {
		field.addClass( "alert alert-danger" );
	      addErrMsg( "Length of " + field_title + " must be between " +
	        min + " and " + max + "." );
	      return false;
	    } else {
	      remErrMsg();
	      return true;
	    }
}
  function checkRegexp( o, regexp, n ) {
    if ( !( regexp.test( o.val() ) ) ) {
      o.addClass( "ui-state-error" );
      addErrMsg( n );
      addErrMsg( "Format of " + n + " must be valid " + "." );
      return false;
    } else {
      remErrMsg();
      return true;
    }
  }
  
  function checkPass()
  {
	  tips = $( ".validateTips" );
      //Store the password field objects into variables ...
      var pass1 = document.getElementById('password');
      var pass2 = document.getElementById('matchingPassword');
      //Store the Confimation Message Object ...
      //var message = document.getElementById('confirmMessage');
      //Set the colors we will be using ...
      var goodColor = "#66cc66";
      var badColor = "#ff6666";
      //Compare the values in the password field 
      //and the confirmation field
      if(pass1.value == pass2.value){
          //The passwords match. 
          //Set the color to the good color and inform
          //the user that they have entered the correct password 
          pass2.style.backgroundColor = goodColor;
          remErrMsg();
          /*message.style.color = goodColor;
          message.innerHTML = "Passwords Match"*/
         // updateTips("Passwords Match");
          
      }else{
          //The passwords do not match.
          //Set the color to the bad color and
          //notify the user.
          pass2.style.backgroundColor = badColor;
          addErrMsg("Password mismatching");
         /* message.style.color = badColor;
          message.innerHTML = "Passwords Do Not Match!"*/
        //  updateTips("Passwords Do Not Match!");
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

