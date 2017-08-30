$(document).ready(function(){

	//Loading POSTS page with pagination
	var searchCriteria = checkSightSeachCriteria();

	//var searchResult = getPaginationPage(searchCriteria, 1, '/posts/pagination-posts');

	var totalPagesVal = $('#pages-total-num').val();

	console.log("totalPagesVal: " + totalPagesVal);
	renderPagination(totalPagesVal);

	function renderPagination(totalPages) {
		$('#page-selection').twbsPagination({
			totalPages: totalPages,
			visiblePages: 3,
			onPageClick: function (event, page) {
				console.log('pagination tab is clicked, page # ' + page);
				searchResult = getPaginationPage(searchCriteria, page, '/posts/pagination-posts');
				totalPages = searchResult.totalPages;
			}
		});
	}

});

function checkSightSeachCriteria() {
	var searchCriteria = null;
	var sightIdVal = $("#sight_id").val();
	if(typeof sightIdVal != 'undefined') {
		searchCriteria = {'BY_SIGHT_ID' : parseInt(sightIdVal)};
	}
	return searchCriteria;
}

function getPaginationPage(searchCriteria, currentPage, requestUrl) {
	var searchAttributes = {};
	searchAttributes['searchCriteria'] = searchCriteria;
	searchAttributes['currentPage'] = currentPage;

	var seachResult = null;

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

	$.ajax({
		url: requestUrl,
		type:"POST",
		data: JSON.stringify(searchAttributes),
		contentType:"application/json; charset=utf-8",
		success: function(data){
			$("#posts-block").replaceWith(data.results);
			seachResult = data;
		},
		error : function(e) {
			console.log(e);
		},
		done : function(e) {
			//alert("DONE");
		}
	});
	return seachResult;
}
