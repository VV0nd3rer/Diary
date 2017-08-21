$(document).ready(function(){
	var pagination_content = $("#pagination_handler").val();
	var search_criteria = getSightSearchCriteria(pagination_content);
	getPaginationPage(pagination_content, search_criteria, 1);
});
function getSightSearchCriteria(pagination_content) {
	var search_criteria = null;
	if(pagination_content === 'POSTS') {
		if(typeof $("#sight_id").val() != 'undefined') {
			search_criteria = {"sight_id": parseInt($("#sight_id").val())};
		}
	}
	return search_criteria;
}
function getPaginationPage(pagination_content, search_criteria, page_num) {
	var pagination = {};
	pagination["page_index"] = page_num;
	pagination["pagination_type"] = pagination_content;
	pagination["search_criteria"] = search_criteria;
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		url: "/pagination",
		type:"POST",
		data: JSON.stringify(pagination),
		contentType:"application/json; charset=utf-8",
		//dataType:"json",
		success: function(data){
			$("#posts-block").replaceWith(data);
			redrawPagination($('#pages-total-num').val());
		},
		error : function(e) {
			console.log(e);
		},
		done : function(e) {
			//alert("DONE");
		}
	});
}
function redrawPagination(totalPages) {
	$('#page-selection').twbsPagination({
		totalPages: totalPages,
		visiblePages: 3,
		onPageClick: function (event, page) {
			var pagination_content = $("#pagination_handler").val();
			var search_criteria = getSightSearchCriteria(pagination_content);
			getPaginationPage(pagination_content, search_criteria, page);
		}
	});
}