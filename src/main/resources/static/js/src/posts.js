/**
 * Created by Kverchi on 24.6.2018.
 */
$(document).ready(function(){

    var currentPage;

    var paginationElement;
    var paginationURL;
    var paginationChangingBlock;

    if($('#post-pagination').length) {
        paginationElement = $('#post-pagination');
        paginationURL = '/posts/pagination-posts';
        paginationChangingBlock = 'posts-block';
        initializePaginationPlagin();
    }
    function initializePaginationPlagin() {
        var defOpts = renderPaginationPlagin();
        paginationElement.twbsPagination(defOpts);
        paginationElement.trigger('page');
    }
    function renderPaginationPlagin() {
        var defaultOpts = {
            visiblePages: 3,
            initiateStartPageClick: false,
            onPageClick: function (event, page) {
                console.log("pgn clicked " + page);
                var pagination = {};
                pagination['currentPage'] = page;

                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                $(document).ajaxSend(function(e, xhr, options) {
                    xhr.setRequestHeader(header, token);
                });

                $.ajax({
                    url: paginationURL,
                    type:"POST",
                    data: JSON.stringify(pagination),
                    contentType:"application/json; charset=utf-8",
                    success: function(data){
                        console.log("pgn clicked after success " + page);
                        $("#"+paginationChangingBlock).replaceWith(data);
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
}
