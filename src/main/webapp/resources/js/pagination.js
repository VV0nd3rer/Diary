$(document).ready(function(){

	var searchCriteria = checkSightSeachCriteria();
	console.log("search criteria: " + JSON.stringify(searchCriteria));

	var defaultOpts = {
		totalPages: 20,
		visiblePages: 3,
		initiateStartPageClick: false,
		onPageClick: function (event, page) {
			var searchAttributes = {};
			searchAttributes['searchCriteria'] = searchCriteria;
			searchAttributes['currentPage'] = page;

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
			});

			$.ajax({
				url: '/posts/pagination-posts',
				type:"POST",
				data: JSON.stringify(searchAttributes),
				contentType:"application/json; charset=utf-8",
				success: function(data){
					$("#posts-block").replaceWith(data);

					postPagination.twbsPagination('destroy');
					postPagination.twbsPagination($.extend({}, defaultOpts, {
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
	var postPagination = $('#page-selection');
	postPagination.twbsPagination(defaultOpts);
	postPagination.trigger('page');
});

function checkSightSeachCriteria() {
	var searchCriteria = null;
	var sightIdVal = $("#sight_id").val();
	if(typeof sightIdVal != 'undefined') {
		searchCriteria = {'BY_SIGHT_ID' : parseInt(sightIdVal)};
	}
	return searchCriteria;
}

