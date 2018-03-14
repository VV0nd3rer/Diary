var stompClient = null;
var currentPageNum = 1;
connect();

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
   /* var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });*/
    var socket = new SockJS('/messenger');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ');
        /*
         When any websocket connection is open,
         Spring will assign it a session id (not HttpSession, assign per connection).
         And when your client subscribe to an channel start with /user/, eg: /user/queue/reply,
         your server instance will subscribe to a queue named queue/reply-user[session id]
         https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket
         */
        stompClient.subscribe('/user/queue/receive-msg', function (message) {
            renderMessage(JSON.parse(message.body), true);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    var msgText = $("#msg-input").val();
    if(msgText.length < 2) {
        alert("Uh-oh, apparently, you don't know what to say... ");
        return;
    }
    var send_message_url;
    if($('.chat').data('convid') == 0) {
        send_message_url = '/messages/new-conversation/send-message';
    } else {
        send_message_url = '/messages/send-message';
    }
    $.ajax({
        url: send_message_url,
        type: "POST",
        data: JSON.stringify({'text': msgText }),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log('Message was sent :) ');
            $("#msg-input").val('');
            renderMessage(JSON.stringify({'text': msgText, 'sentDatetime': new Date($.now())}), false)
            if(data.length != 0) {
                $('.chat').data('convid', data);
            }
        }
    });
}

function renderMessage(message, isInbox) {
    var messageTemplate;
    var inboxTemplateContent;
    if(isInbox) {
        inboxTemplateContent =
            '<span class="inbox-content">' +
                '<span>' + message.sender.username + ': </span>' +
                '<span class="name">' + message.text + '</span>'
                '<span class="badge pull-right">' + message.sentDatetime + '</span>' +
            '</span>';
        messageTemplate =
            '<li class="left clearfix list-group-item-success" ' +
            '    data-msgid="' + message.messageId + '" ' +
            '    onclick="setMessageAsRead()">' +
                '<div class="pull-left">' +
                    '<div class="header">' +
                    '<div><strong>' + message.sender.username + '</strong></div>' +
                    '<small class=" text-muted">' +
                        '<span class="glyphicon glyphicon-time"></span>' + /*'dd/MM/yyyy HH:mm'*/
                        '<span>now</span>' +
                    '</small>' +
                    '</div>' +
                '<p>' + message.text + '</p>' +
                '</div>' +
            '</li>';
    }
    else {
        message = JSON.parse(message);

        inboxTemplateContent =
            '<span class="inbox-content">' +
                '<span>You: </span>' +
                '<span class="name">' + message.text + '</span>'
                '<span class="badge pull-right">' + message.sentDatetime + '</span>' +
            '</span>';
        messageTemplate =
            '<li class="right clearfix" ' +
            '    data-msgid="' + message.messageId + '" ' +
            '    onclick="setMessageAsRead()">' +
                '<div class="pull-right">' +
                    '<div class="header">' +
                        '<div><strong>Me</strong></div>' +
                        '<small class=" text-muted">' +
                            '<span class="glyphicon glyphicon-time"></span>' + /*'dd/MM/yyyy HH:mm'*/
                            '<span>' + message.sentDatetime + '</span>' +
                        '</small>' +
                    '</div>' +
                    '<p>' + message.text + '</p>' +
                '</div>' +
            '</li>';
    }

    var current_conv_id = $('.chat').data('convid');
    var inbox_message_conv_id;
    if(isInbox) {
      inbox_message_conv_id = message.conversation.conversationId;
        increaseMessagesCounter();
    } else {
        inbox_message_conv_id = current_conv_id;
    }
    if($("#inbox").length != 0) {
        var isNewConversation = true;
        $('#inbox a.list-group-item').each(function () {
            var conv_id = $(this).data("convid");
            if (inbox_message_conv_id == conv_id) {
                $(this).addClass("list-group-item-success").removeClass("list-group-item-warning");
                $(this).find('.inbox-content').replaceWith(inboxTemplateContent);
                isNewConversation = false;
            }
        });
        if (isNewConversation) {
            var inboxTemplate =
                '<a class="list-group-item list-group-item-success" ' +
                'data-convid="' + message.conversation.conversationId + '" onclick=loadConversation(this)>' +
                '<span>' + message.conversation.user1.username + '</span>' +
                '<span> & </span>' +
                '<span class="name">' + message.conversation.user2.username + '</span>' +
                inboxTemplateContent +
                '</a>'
            $('#inbox').prepend(inboxTemplate);
        }
    }
    if(current_conv_id == inbox_message_conv_id) {
        $("ul[class='chat']").prepend(messageTemplate);
    }

    if(!isInbox) {
        $("#msg-container").stop().animate({scrollTop: 0}, 1000);
    }
}
function increaseMessagesCounter() {
    var current_total_msg_counter = parseInt($('#total-unread-msg-counter em').html(), 10) || 0;
    var current_conversation_msg_counter = parseInt($('#unread-msg-counter em').html(), 10) || 0;
    current_total_msg_counter += 1;
    current_conversation_msg_counter += 1;
    $('#total-unread-msg-counter em').html('+' + current_total_msg_counter);
    $('#unread-msg-counter em').html('+' + current_conversation_msg_counter);
}
function decreaseMessagesCounter(val) {
    var current_total_msg_counter = parseInt($('#total-unread-msg-counter em').html(), 10) || 0;
    var current_conversation_msg_counter = parseInt($('#unread-msg-counter em').html(), 10) || 0;
    current_total_msg_counter -= val;
    current_conversation_msg_counter -= val;
    $('#total-unread-msg-counter em').html(current_total_msg_counter > 0 ? '+' + current_total_msg_counter : '');
    $('#unread-msg-counter em').html(current_conversation_msg_counter > 0 ? '+' + current_conversation_msg_counter : '');
}
function loadMoreMessages() {
    currentPageNum++;
    var conversation_more_url = "/messages/conversation/more/" + currentPageNum;
    $.get(conversation_more_url, function (data) {
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
        },
        error: function(e) {
            console.log('Error ' + e);
        },
        complete: function() {
            console.log('Complete ');
        }
    });
    decreaseMessagesCounter(readMessagesId.length);
    return;
}
function loadConversation(element) {
    var conv_id = element.getAttribute('data-convid');
    var conversation_url = "/messages/conversation/" + conv_id;
    $.get(conversation_url, function (data) {
        $("#inbox").replaceWith(data);
        //$("#msg-container").stop().animate({ scrollTop: $("#msg-container")[0].scrollHeight}, 1000);
    });
}
function createConversation() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    var create_conversation_url = "/messages/conversation/new";
    var authSuggestInput = $("#authSuggestInput");
    var authSuggestHidden = $('#' + authSuggestInput.attr("id") + '-hidden');
    var user = {};
    user["userId"] = parseInt(authSuggestHidden.val());
    user["username"] =  authSuggestInput.val();
   $.ajax({
        url: create_conversation_url,
        type: "POST",
        data: JSON.stringify(user),
        contentType: "application/json; charset=utf-8",
        //dataType:"json",
        success: function (data) {
            $("#inbox").replaceWith(data);
        },
        error: function(e) {
            console.log('Error ');
        },
        complete: function() {
            console.log('Complete ');
        }
    });
}
$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    //$( "#send" ).click(function() { sendMessage(); });

});
$(document).ready(function() {
    $("#btn-msg-send").click(function() {
        e.preventDefault();
        sendMessage();
    });
    $("#new-conversation").click(function() {
        createConversation();

        return;
    });
    $("#all-msgs").click(function(e) {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        var all_messages_url = "/messages/all";
        $.get(all_messages_url, function (data) {
            if($("#inbox").length == 0) {
                $("#conversation").replaceWith(data);
            } else {
                $("#inbox").replaceWith(data);
            }
        });
        /*$.ajax({
            url: all_messages_url,
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            success: function (data) {
                alert(JSON.stringify(data));
               $("#conversation").replaceWith(data);
            }
        });*/
        return;
    });
});