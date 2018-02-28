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
    currentPageNum++;
    console.log('current page num: ' + currentPageNum);
    var conversation_more_url = "/messages/conversation/more/" + currentPageNum;
    $.get(conversation_more_url, function (data) {
        console.log(data === '');
        if(data === '') {
            $('#more-messages').replaceWith("No more messages");
        } else {
            $('ul[class="chat"]').append(data);
            //$("#msg-container").stop().animate({ scrollTop: $("#msg-container")[0].scrollHeight}, 1000);
        }
    });
    return false;
}
function setMessageAsRead() {
    var readMessagesId = [];

    $('.chat li.list-group-item-success').each(function() {
        var msg_id = $(this).data("msgid");
        readMessagesId.push(msg_id);
        alert(msg_id);
    });
    $('.chat li.list-group-item-success').removeClass("list-group-item-success");
    var update_messages_url = "/messages/conversation/set-read";

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        url: update_messages_url,
        type: "POST",
        data: JSON.stringify(readMessagesId),
        contentType: "application/json; charset=utf-8",
        dataType:"json",
        success: function () {
            console.log('Messages were updated.');
        }
    });
    return;
}
