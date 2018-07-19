/**
 * Created by Kverchi on 24.6.2018.
 */

$(document).ready(function () {

    var paginationElement = $('#post-pagination');
    var paginationUrl = '/posts/paginated-posts';
    var paginationTextChangingBlock = 'posts-block';
    var searchAttributes;

    initializePaginationPlagin(paginationElement, paginationUrl, paginationTextChangingBlock);

    $("#sightSuggestionInput").focusout(function () {
        var input = $(this);
        console.log("focusout: " + $(this).val());
        checkDataListInput(input);
    });

    $("#search-post-btn").click(function (e) {
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

        paginationUrl = "/posts/search-paginated-posts";

        desrtoyPaginationPlagin(paginationElement);
        initializePaginationPlagin(paginationElement, paginationUrl, paginationTextChangingBlock, searchAttributes);
    });
});

function initializePaginationPlagin(paginationElement, paginationUrl, paginationTextChangingBlock, searchAttributes) {
    var paginationOptions = {
        totalPages: 35,
        visiblePages: 3,
        initiateStartPageClick: false,
        onPageClick: function (event, page) {
            if (typeof searchAttributes === 'undefined') {
                $.get(paginationUrl + "/" + page, function (data) {
                    console.log("pgn clicked after success " + page);
                    $("#" + paginationTextChangingBlock).replaceWith(data);
                    currentPage = page;
                    paginationElement.twbsPagination('destroy');
                    paginationElement.twbsPagination($.extend({}, paginationOptions, {
                        totalPages: $("#" + paginationTextChangingBlock).find("span").attr("data-total-pages"),
                        startPage: page
                    }));
                });
                console.log("total pages: " + $("#posts-block").find("span").attr("data-total-pages"));
            }
            else {
                var postSearchRequest = {};
                postSearchRequest["searchAttributes"] = searchAttributes;
                postSearchRequest["currentPage"] = page;
                $.ajax({
                    url: paginationUrl,
                    type: "POST",
                    data: JSON.stringify(postSearchRequest),
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {
                        console.log("pgn clicked after success " + page);
                        $("#" + paginationTextChangingBlock).replaceWith(data);
                        currentPage = page;
                        paginationElement.twbsPagination('destroy');
                        paginationElement.twbsPagination($.extend({}, paginationOptions, {
                            totalPages: $("#" + paginationTextChangingBlock).find("span").attr("data-total-pages"),
                            startPage: page
                        }));
                    },
                    error: function (e) {
                        console.log(e);
                    },
                    done: function (e) {
                        //alert("DONE");
                    }
                });
            }
        }
    };
    paginationElement.twbsPagination(paginationOptions);
    paginationElement.trigger('page', 1);
}

function desrtoyPaginationPlagin(paginationElement) {
    paginationElement.twbsPagination('destroy');
}

function checkDataListInput(obj) {
    var val = obj.val();
    var hiddenInput = $("#" + obj.attr("id") + '-hidden');
    var res = false;
    if (val === '') {
        hiddenInput.val('');
        return res;
    }
    console.log("checking data list input...");

    var listId = obj.attr('list');
    var options = $('#' + listId + ' option');

    var hiddenInput = $("#" + obj.attr("id") + '-hidden');


    tips = $(".validateTips");
    res = isDataListInput(options, val, hiddenInput);
    if (res) {
        tips.hide();
    }
    else {
        tips.show();
    }
    return res;
}
function isDataListInput(options, val, hiddenInput) {
    var res = false;

    for (var i = 0; i < options.length; i++) {
        var option = options[i];
        if (option.innerText === val) {
            console.log("valid");
            var dataValAttr = option.getAttribute('data-value');
            if (dataValAttr != null) {
                hiddenInput.val(dataValAttr);
            }
            res = true;
            break;
        }
    }
    return res;
}