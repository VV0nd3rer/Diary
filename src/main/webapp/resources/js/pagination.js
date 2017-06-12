$(document).ready(function(){
	var root = '/Diary';
	$('#page-selection').bootpag({
        total: $('#pages-total-num').val(),
        page: 1,
        maxVisible: 5,
        leaps: true,
        firstLastUse: true,
        wrapClass: 'pagination',
        activeClass: 'active',
       // disabledClass: 'disabled',
        nextClass: 'next',
        prevClass: 'prev',
        lastClass: 'last',
        firstClass: 'first'
    }).on("page", function(event, num){
    	//if total page is loaded from db, disabled doesn't work :(
    	if(num == $('#pages-total-num').val()) {   		
    		return;
    	}
    	//
    	var pagination_type="posts";
    	var paginated_url = root + "/pagination";
    	var pagination_handler = $("#pagination_handler").val(); 
    	var pagination = {};
    	pagination["page_index"] = num;
    	pagination["pagination_type"] = pagination_type;
    	if(pagination_handler == "posts") { 
    		pagination["search_criteria"] = null;
    	}
    	if(pagination_handler == "sight_posts") {
    		pagination["search_criteria"] = {"sight_id": $("#sight_id").val()};
    	}
		var token = $("meta[name='_csrf']").attr("content");
	 	var header = $("meta[name='_csrf_header']").attr("content");
	 	$(document).ajaxSend(function(e, xhr, options) {
	        xhr.setRequestHeader(header, token);
	    });
		
    	$.ajax({
	     	   url: paginated_url,
	     	   type:"POST",
	     	   data: JSON.stringify(pagination),
	     	   contentType:"application/json; charset=utf-8",
	     	   dataType:"json",
	     	   success: function(data){
		     		$( ".blog-post" ).remove();
		      		var post = data.pagePosts;
		      		var pages_total_num = data.pages_total_num;
		      		
		      		//$("#pages-total-num").val(pages_total_num);
		      		var postHTML = '<div class="col-sm-8 blog-main">';
		      		$.each(post, function(index) {
		      			var single_post_url = root +"/posts/single-post/"+post[index].post_id;
		      			postHTML += '<div class="blog-post">';
		      			postHTML += '<h2 class="blog-post-title">'+post[index].title+'</h2>';
		      			postHTML += '<input type="hidden" id="sight_id" value="'+post[index].sight_id+'"/>;'
		      			postHTML += '<p class="lead">by ' + post[index].user.username+'</p>';
		      			postHTML += '<p class="blog-post-meta">'+post[index].post_id+'</p>';
		      			//(data[index].description == null) ? '' : data[index].description
		      			postHTML += '<blockquote><p>'+post[index].description +'</p></blockquote>';
		      			postHTML += '<p><a class="btn btn-default" href="'+single_post_url+'"role="button">View details &raquo;</a></p>';
		      			postHTML += '</div>';
		      	    });
		      		$(".row").html(postHTML);
	     	   },
	     	   error : function(e) {
	    			console.log(e);
	 	   		},
	 	   		done : function(e) {
	 	   			alert("DONE");
	 	   		}
	     	 });
        $("#paginator").html("Page " + num); // or some ajax content loading...
    }); 
});
