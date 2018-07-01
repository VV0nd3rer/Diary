/**
 * Created by Kverchi on 24.6.2018.
 */
$(document).ready(function(){
    var paginatedElement = $('#post-pagination');
    var postsUrl = '/posts/paginated-posts';
    var htmlChangingBlock = 'posts-block';
    initializePaginationPlagin(paginatedElement, postsUrl, htmlChangingBlock);
});

function initializePaginationPlagin(paginatedElement, postsUrl, htmlChangingBlock, searchAttr) {
    var paginationOptions = {
        totalPages: 35,
        visiblePages: 3,
        initiateStartPageClick: false,
        onPageClick: function (event, page) {
            console.log("pgn clicked " + page);
            if(searchAttr === undefined) {
                console.log('searchAttr is undefined');
                $.get(postsUrl + "/" + page, function(data){
                    console.log("pgn clicked after success " + page);
                    $("#"+htmlChangingBlock).replaceWith(data);
                    currentPage = page;
                    paginatedElement.twbsPagination('destroy');
                    paginatedElement.twbsPagination($.extend({}, paginationOptions, {
                        totalPages: $("#"+htmlChangingBlock).find("span").attr("data-total-pages"),
                        startPage: page
                    }));
                });
                console.log("total pages: " + $("#posts-block").find("span").attr("data-total-pages"));
            }
        }
    };
    paginatedElement.twbsPagination(paginationOptions);
    paginatedElement.trigger('page', 1);
}
