$(document).ready(function(){

	$('.nav').on('click', 'li', function() {
		$('.nav li.active').removeClass('active');
		$(this).addClass('active');
	});

	var root = '';

	/*$("#catSuggestInput").keyup(function(e) {
		if (isLetterOnNumberClicked(e)) {
			var url = "/sights/search-sight";
			loadDataListFromDB(url, $(this));
		}
	});*/

	var searchCriteria = checkSightSeachCriteria();
	var postSortType = null;
	var currentPage;


	/*var postPagination = $('#post-pagination');
	var bookPagination = $('#book-pagination');*/
	var paginationElement;
	var paginationURL;
	var paginationChangingBlock;
	if($('#post-pagination').length) {
		paginationElement = $('#post-pagination');
		paginationURL = '/posts/pagination-posts';
		paginationChangingBlock = 'posts-block';
		initializePaginationPlagin();
	}
	if($('#book-pagination').length) {
		paginationElement = $('#book-pagination');
		paginationURL = '/books/pagination-books';
		paginationChangingBlock = 'books-block';
		initializePaginationPlagin();
	}
	/*if($('#message-pagination').length) {
		paginationElement = $('#message-pagination');
		paginationURL = '/messages/pagination-messages';
		paginationChangingBlock = 'messages-block';
		initializePaginationPlagin();
	}*/
	function initializePaginationPlagin() {
		var defOpts = renderPaginationPlagin();
		paginationElement.twbsPagination(defOpts);
		paginationElement.trigger('page');

		//renderPaginationPlagin(url, blockID);
	}
	function renderPaginationPlagin() {
		var defaultOpts = {
			//totalPages: 20,
			visiblePages: 3,
			initiateStartPageClick: false,
			onPageClick: function (event, page) {
				console.log("pgn clicked " + page);
				var searchAttributes = {};
				var pagination = {};
				pagination['currentPage'] = page;
				searchAttributes['searchCriteria'] = searchCriteria;
				searchAttributes['pagination'] = pagination;
				searchAttributes['postSortType'] = postSortType;

				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$(document).ajaxSend(function(e, xhr, options) {
					xhr.setRequestHeader(header, token);
				});

				$.ajax({
					url: paginationURL,
					type:"POST",
					data: JSON.stringify(searchAttributes),
					contentType:"application/json; charset=utf-8",
					success: function(data){
						console.log("pgn clicked after success " + page);
						$("#"+paginationChangingBlock).replaceWith(data);
						if(paginationChangingBlock === 'books-block') {
							$("#update-book-btn").prop("disabled", true);
							$("#delete-book-btn").prop("disabled", true);
							initializeCRUDTableClickListener();
						}
						currentPage = page;
						paginationElement.twbsPagination('destroy');
						paginationElement.twbsPagination($.extend({}, defaultOpts, {
							totalPages: $('#total-pages').val(),
							startPage: page
						}));

					},
					error : function(e) {
						console.log(e);
					},
					done : function(e) {
						//alert("DONE");
					}
				});
			}
		};
		return defaultOpts;
	}

	var authCorrectInput = null;
	var catCorrectInput = null;

	$("#authSuggestInput").focusout(function () {
		var input = $(this);
		console.log("focusout " + $(this).val());
		authCorrectInput = null;
		if(checkDataListInput(input)) {
			authCorrectInput = input;
		}
	});
	$("#catSuggestInput").focusout(function () {
		var input = $(this);
		console.log("focusout: " + $(this).val());
		catCorrectInput = null;
		if(checkDataListInput(input)) {
			catCorrectInput = input;
			$("#filterSights").prop("disabled", true);
		} else {
			$("#filterSights").prop("disabled", false);
		}
	});

	$("#search-post-btn").click(function(e) {
		e.preventDefault();

		var catSuggestInput = $("#catSuggestInput");
		var authSuggestInput = $("#authSuggestInput");
		var catSuggestHidden = $('#' + catSuggestInput.attr("id") + '-hidden');
		var authSuggestHidden = $('#' + authSuggestInput.attr("id") + '-hidden');
		var searchText = $('#searchInTextInput');
		var searchInTitleOnlyCheckBox = $('#searchCondition');
		var filterSightsDropBox = $('#filterSights');
		searchCriteria = {};

		if(catCorrectInput != null) {
			searchCriteria["BY_SIGHT_ID"] = parseInt(catSuggestHidden.val());
		}
		if(authCorrectInput != null) {
			searchCriteria["BY_USER_ID"] = parseInt(authSuggestHidden.val());
		}
		if(searchText.val() != '') {
			searchInTitleOnlyCheckBox.is(":checked") ? searchCriteria["IN_TITLE_ONLY"] = searchText.val() :
				searchCriteria["BY_TEXT"] = searchText.val();
		}
		if(filterSightsDropBox.val() > 0) {
			filterSightsDropBox.val() == 1 ? postSortType = "BY_WISHES" : postSortType = "BY_VISITS";
		}
		paginationElement.trigger('page');
	});


	$("#search-book-btn").click(function(e) {
		e.preventDefault();

		catCorrectInput = null;
		searchCriteria = checkSightSeachCriteria();

		var authSuggestInput = $("#authSuggestInput");
		var authSuggestHidden = $('#' + authSuggestInput.attr("id") + '-hidden');

		var searchText = $('#searchInTextInput');
		var searchInTitleOnlyCheckBox = $('#searchCondition');
		searchCriteria = {};

		if(authCorrectInput != null) {
			searchCriteria["BY_AUTHOR_ID"] = parseInt(authSuggestHidden.val());
		}
		if(searchText.val() != '') {
			searchInTitleOnlyCheckBox.is(":checked") ? searchCriteria["IN_TITLE_ONLY"] = searchText.val() :
				searchCriteria["BY_TEXT"] = searchText.val();
		}
		paginationElement.trigger('page');
	});
	$("#add-wish-btn").click(function(event) {
		var add_wish_url = '/posts/add-wish';
		$.get(add_wish_url, function(data) {
			$("#add-wish-btn").replaceWith(data);
		});
	});
	$("#add-visit-btn").click(function(event) {
		var add_visit_url = '/posts/add-visit';
		$.get(add_visit_url, function(data) {
			$("#add-visit-btn").replaceWith(data);
		});
	});
	$("#create-post-ok-btn").click(function(event) {
		event.preventDefault();
		var description = tinyMCE.get('descrEditor').getContent();
		var postText = tinyMCE.get('textEditor').getContent();
		var id = $("#id");
		//var sight_id = $("#sight").find(":selected");
		var title = $("#title");
		//var description = $("#description");
		//console.log("sight id: " + sight_id.val());
		var data = {}
		var sight = {}
		//data["sight_id"] = sight_id.val();
		data["postId"] = id.val();
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
		comment.postId = post_id.val();
	
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
	     	   data: JSON.stringify(comment),
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

	$("#modal-form-save-info-btn").click(function(event){
		event.preventDefault();
		var info = $("#edit-info").val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});

		var save_info_url = root+"/users/save-info/";

			$.ajax({
				url: save_info_url,
				type:"POST",
				data: info,
				contentType: "text/plain",
				success: function(res){
					$("#current-info").text(info);
					hideModalDialog();
				},
				error : function(e) {
					console.log("Error: ", e);
				},
				done : function(e) {
					alert("DONE");
				}
			});

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
    	var upd_book_url = root + "/books/edit/"+id;
    	$.get(upd_book_url, function(data) {
    		//event.preventDefault();
    		$("#id").val(id);
    		$("#title").val(data.title);
    		$("#description").val(data.description);
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
    	var rm_book_url = root+"/books/remove/"+id;
    	$.get(rm_book_url, function(data) {
    		$("#crud-tbl tr:contains('" + id + "')").remove();
				paginationElement.trigger('page', [currentPage]);
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
    	var rm_sight_url = root+"/sights/remove/"+id;
    	$.get(rm_sight_url, function(data) {
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
    	data["bookId"] = id.val();
    	data["title"] = title.val();
        data["description"] = description.val();
        data["author"] = author.val();
        
        tips = $( ".validateTips" );
        var valid = true;
        valid = valid && checkMinMaxLength( title, "title", 3, 255 );
        //valid = valid && checkMinMaxLength( description, "description", 3, 80 );
        valid = valid && checkMinMaxLength( author, "author", 2, 100 );
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
	     		    //$("#crud-tbl tbody").append("<tr class='clickable-row'><td>"+obj.book_id+"</td><td>"+obj.book_title+"</td><td>"+obj.author+"</td></tr>");
				   paginationElement.trigger('page', [currentPage]);
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


    /*$('#crud-tbl').on('click', '.clickable-row', function(event) {
    	  console.log('click on crud-tbl class');
		  $('#update-book-btn').removeAttr('disabled');
		  $('#delete-book-btn').removeAttr('disabled');
    	  $(this).addClass('danger').siblings().removeClass('danger');
    	  //var valur =$(this).find('td:first').html();
    	   //alert(value);    
    });	 */
   $('table').on('click', '.row_action', function(event) {
	   event.preventDefault();
	   var id = $("#crud-tbl tr.danger").find('td:first').html();
	   window.location.replace("posts/sight_posts?sight_id="+id);
   });
   //--- End of Books page CRUD functionality

});
//End of document.ready
function showUserInfoModalDialog() {
	var currentInfo = $("#current-info").text();
	$("#edit-info").val(currentInfo);
	$("#modal-form").modal();
}
function showUpdatePasswordModalDialog() {
	$("#pass-modal-form").modal();
}

function hideModalDialog() {
	console.log('hiding modal dialog...')
	$("#modal-form").modal("hide");
	$(':input','#modal-form')
    .not(':button, :submit, :reset, :hidden')
    .val('')
    .removeAttr('checked')
    .removeAttr('selected');
	$(':input[type=hidden]','#modal-form').val('0');
	console.log('hided modal dialog');
}
function addErrMsgToField( text, object ) {
    tips
      .text( text )
      .addClass( "alert alert-danger" );
	object.addClass( "alert alert-danger" );
    setTimeout(function() {
		object.removeClass("alert alert-danger", 1500);
        tips.removeClass( "alert alert-danger", 1500 );
    	tips.empty();
    }, 500 );
}
function addErrMsg( text) {
	tips
		.text( text )
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
    if ( o.val().length > max || o.val().length < min ) {

      addErrMsgToField( "Length of " + n + " must be between " +
        min + " and " + max + ".", o);
      return false;
    }/* else {
      remErrMsg();
      return true;
    }*/
	return true;
  }
function checkTextEditorMinLength(content, field, field_title, min) {
	if (content.length < min) {
		addErrMsgToField( "Length of " + field_title + " must be mere than " +
			min + ".", field);
		return false;
	} /*else {
		remErrMsg();
		return true;
	}*/
	return true;
}
function checkTextEditorMinMaxLength(content, field, field_title, min, max ) {
	if (content.length > max || content.length < min) {

	      addErrMsgToField( "Length of " + field_title + " must be between " +
	        min + " and " + max + ".", field);
	      return false;
	    } /*else {
	      remErrMsg();
	      return true;
	    }*/
	return true;
}
function checkTextAreaMinMaxLen(object, name, min, max) {
	var val = $.trim(object.val());
	if(val < min || val > max) {
		addErrMsgToField( "Length of " + name + " must be between " +
			min + " and " + max + ".", object);
		return false;
	}
	return true;

}
function checkDataListInput(obj) {
	var val = obj.val();
	var hiddenInput = $("#" + obj.attr("id") + '-hidden');
	var res = false;
	if(val === '') {
		hiddenInput.val('');
		return res;
	}
	console.log("checking data list input...");

	var listId = obj.attr('list');
	var options = $('#' + listId + ' option');

	var hiddenInput = $("#" + obj.attr("id") + '-hidden');


	tips = $( ".validateTips" );
	res = isDataListInput(options, val, hiddenInput);
	if(res) {
		tips.hide();
	}
	else {
		tips.show();
	}
	return res;
}
function isDataListInput(options, val, hiddenInput) {
	var res = false;

	for (var i = 0; i < options.length; i++) {
		var option = options[i];
		if (option.innerText === val) {
			console.log("valid");
			var dataValAttr = option.getAttribute('data-value');
			if(dataValAttr != null) {
				hiddenInput.val(dataValAttr);
			}
			res = true;
			break;
		}
	}
	return res;
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

function loadDataListFromDB(url, element) {
	var inputValue = element.val();
	/*var options = $("option");
	var inputId = element.attr('id');*/
	var listId = element.attr('list');
	//var hiddenInput = $(inputId + '-hidden');
	$.get(url, { search_str: inputValue }).done (function (data) {
		var options = '';
		$.each(data, function (index, value) {
			options += '<option data-value="' + value.sight_id + '">' + value.sight_label + '</option>';
		});
		$('#'+listId).empty();
		$('#'+listId).append(options);
	});
	/*for (var i = 0; i < options.length; i++) {
		var option = options[i];

		if (option.innerText === inputValue) {
			hiddenInput.value = option.getAttribute('data-value');
			console.log("option.getAttribute('data-value'): " + option.getAttribute('data-value'));
			break;
		}
	}*/
}
function isLetterOnNumberClicked(e) {
	return (e.which <= 90 && e.which >= 48) || (e.which <= 105 && e.which >= 96);
}
function checkSightSeachCriteria() {
	var searchCriteria = null;
	var sightIdVal = $("#sight-id").val();
	if(typeof sightIdVal != 'undefined') {
		searchCriteria = {'BY_SIGHT_ID' : parseInt(sightIdVal)};
	}
	return searchCriteria;
}
function initializeCRUDTableClickListener() {
	$('#crud-tbl').on('click', '.clickable-row', function(event) {
		$('#update-book-btn').removeAttr('disabled');
		$('#delete-book-btn').removeAttr('disabled');
		$(this).addClass('danger').siblings().removeClass('danger');
		//var valur =$(this).find('td:first').html();
		//alert(value);
	});
}
