$(document).ready(function(){
	var root = '';
	//var pages_total_num = $('#pages-total-num').val();
	$('#page-selection').bootpag({
        total: $('#pages-total-num').val(),
        page: 1,
        maxVisible: 5,
        leaps: true,
        firstLastUse: true,
        wrapClass: 'pagination',
        activeClass: 'active',
        disabledClass: 'disabled',
        nextClass: 'next',
        prevClass: 'prev',
        lastClass: 'last',
        firstClass: 'first'
    }).on("page", function(event, num){
    	var pagination_type="posts";
    	var paginated_url = root + "/pagination";
    	var pagination_handler = $("#pagination_handler").val();
		console.log("pagination handler : " + pagination_handler);
    	var pagination = {};
    	pagination["page_index"] = num;
    	pagination["pagination_type"] = pagination_type;
    	if(pagination_handler == "posts") { 
    		pagination["search_criteria"] = null;
    	}
    	if(pagination_handler == "sight_posts") {
			console.log("sight id: " + parseInt($("#sight_id").val()));
    		pagination["search_criteria"] = {"sight_id": parseInt($("#sight_id").val())};
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
	     	   //dataType:"json",
	     	   success: function(data){
	     		   $("#posts-block").replaceWith(data);
	     	   },
	     	   error : function(e) {
	    			console.log(e);
	 	   	   },
	 	   	   done : function(e) {
	 	   			//alert("DONE");
	 	   	   }
	     	 });
    	$(this).bootpag({total: $("#pages-total-num").val()});      
    }); 
});
