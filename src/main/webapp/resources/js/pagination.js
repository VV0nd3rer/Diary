$(document).ready(function(){
	var root = '/Diary';
	$('#page-selection').bootpag({
        total: $('#pages-total-num').val(),
        page: 1,
        maxVisible: 5,
        leaps: true,
        firstLastUse: true,
        //first: '←',
        //last: '→',
        wrapClass: 'pagination',
        activeClass: 'active',
        disabledClass: 'disabled',
        nextClass: 'next',
        prevClass: 'prev',
        lastClass: 'last',
        firstClass: 'first'
    }).on("page", function(event, num){
    	var posts_page_url = root + "/posts/page/" + num;
    	$.get(posts_page_url, function(data) {
    		$( ".blog-post" ).remove();
    		var post = data.pagePosts;
    		var pages_total_num = data.pages_total_num;
    		
    		//$("#pages-total-num").val(pages_total_num);
    		var postHTML = '<div class="col-sm-8 blog-main">';
    		$.each(post, function(index) {
    			var single_post_url = root +"/posts/single-post/"+post[index].post_id;
    			postHTML += '<div class="blog-post">';
    			postHTML += '<h2 class="blog-post-title">'+post[index].title+'</h2>';
    			postHTML += '<p class="lead">by ' + post[index].user.username+'</p>';
    			postHTML += '<p class="blog-post-meta">'+post[index].post_id+'</p>';
    			//(data[index].description == null) ? '' : data[index].description
    			postHTML += '<blockquote><p>'+post[index].description +'</p></blockquote>';
    			postHTML += '<p><a class="btn btn-default" href="'+single_post_url+'"role="button">View details &raquo;</a></p>';
    			postHTML += '</div>';
    	    });
    		$(".row").html(postHTML);
    	})
    	  .done(function() {
		    console.log("done");
		  })
		  .fail(function(e) {
		    console.log( "error" + e );
		  })
    	
        $("#paginator").html("Page " + num); // or some ajax content loading...
    }); 
});