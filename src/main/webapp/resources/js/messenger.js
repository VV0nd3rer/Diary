/**
 * Created by Liudmyla Melnychuk on 17.1.2018.
 */
var currentPageNum = 1;
$(document).ready(function(){

    $("a.list-group-item").click(function(e) {
        e.preventDefault();
        console.log("inbox div clicked!");
        var id = $(this).find(".conversationId").text();
        console.log('conversation ID: ' + id);
        var conversation_url = "/messages/conversation/" + id;
        console.log(conversation_url);
        $.get(conversation_url, function (data) {
            $("#conversation").replaceWith(data);
            //$("#msg-container").stop().animate({ scrollTop: $("#msg-container")[0].scrollHeight}, 1000);
        });

    });
});
function loadMoreMessages() {
    event.preventDefault();
    currentPageNum++;
    console.log('current page num: ' + currentPageNum);
    var conversation_more_url = "/messages/conversation/more/" + currentPageNum;
    $.get(conversation_more_url, function (data) {
        $('ul[class="chat"]').append(data);
        //$("#msg-container").stop().animate({ scrollTop: $("#msg-container")[0].scrollHeight}, 1000);
    });

}
