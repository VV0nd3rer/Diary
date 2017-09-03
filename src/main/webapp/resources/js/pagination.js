$(document).ready(function() {

	var defaultOpts = {
		//totalPages: 20,
		visiblePages: 3,
		initiateStartPageClick: false,
		onPageClick: function (event, page) {
			console.log("pgn clicked " + page);
			var searchAttributes = {};
			searchAttributes['searchCriteria'] = null;
			searchAttributes['currentPage'] = page;

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$(document).ajaxSend(function(e, xhr, options) {
				xhr.setRequestHeader(header, token);
			});

			$.ajax({
				url: '/books/pagination-books',
				type:"POST",
				data: JSON.stringify(searchAttributes),
				contentType:"application/json; charset=utf-8",
				success: function(data){
					console.log("pgn clicked after success " + page);
					$("#books-block").replaceWith(data);

					$('#book-pagination').twbsPagination('destroy');
					console.log('start page: ' + page);
					console.log('total pages: ' + $('#total-pages').val());
					$('#book-pagination').twbsPagination($.extend({}, defaultOpts, {
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
	$('#book-pagination').twbsPagination(defaultOpts);
	$('#book-pagination').trigger('page');
});
