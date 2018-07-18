/**
 * Created by Kverchi on 24.6.2018.
 */
$(document).ready(function(){

    var paginatedElement = $('#post-pagination');
    var postsUrl = '/posts/paginated-posts';
    var htmlChangingBlock = 'posts-block';
    initializePaginationPlagin(paginatedElement, postsUrl, htmlChangingBlock);

    $("#search-post-btn").click(function(e) {
        e.preventDefault();

        var sightSuggestionInput = $('#sightSuggestionInput');
        var sightSuggestionHidden = $('#' + sightSuggestionInput.attr('id') + '-hidden');

        var searchInTextInput = $('#searchInTextInput');
        var searchInTitleOnlyConditionCheckBox = $('#searchInTitleOnlyCondition');

        var sortingSightsDropBox = $('#sortingSights');

        searchAttributes = {};

        if (sightSuggestionInput.val() != '') {
            searchAttributes["BY_SIGHT_ID"] = parseInt(sightSuggestionHidden.val());
        }
        /*if (authCorrectInput != null) {
            searchAttributes["BY_USER_ID"] = parseInt(authSuggestHidden.val());
        }*/
        if (searchInTextInput.val() != '') {
            /*searchInTitleOnlyCheckBox.is(":checked") ? searchAttributes["IN_TITLE_ONLY"] = searchText.val() :*/
                searchAttributes["BY_TEXT"] = searchInTextInput.val();
        }
        /*if (filterSightsDropBox.val() > 0) {
            filterSightsDropBox.val() == 1 ? postSortType = "BY_WISHES" : postSortType = "BY_VISITS";
        }*/
        /*paginationElement.trigger('page');*/
        console.log('search attrs: ' + JSON.stringify(searchAttributes));
        initializePaginationPlagin(paginatedElement, "/posts/search-paginated-posts", htmlChangingBlock, searchAttributes);
    });
});

function initializePaginationPlagin(paginatedElement, postsUrl, htmlChangingBlock, searchAttr) {
    var paginationOptions = {
        totalPages: 35,
        visiblePages: 3,
        initiateStartPageClick: false,
        onPageClick: function (event, page) {
            console.log("pgn clicked " + page);
            //TODO describe POST method for searching post by searchAttr
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
