var stompClient = null;
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
        console.log('Connected: ' + frame);
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
    //stompClient.send("/app/send-msg", {}, JSON.stringify({'text': "hello!!!"}));
    var msgText = $("#msg-input").val();
    if(msgText.length < 2) {
        alert("Uh-oh, apparently, you don't know what to say... ");
        return;
    }
    $.ajax({
        url: '/messages/send-message',
        type: "POST",
        data: JSON.stringify({'text': msgText }),
        contentType: "application/json; charset=utf-8",
        success: function () {
            console.log('Message was sent :) ');
            $("#msg-input").val('');
            renderMessage(JSON.stringify({'text': msgText, 'sentDatetime': new Date($.now())}), false)
        }
    });
}

function renderMessage(message, isInbox) {
    var messageTemplate;
    var inboxTemplateContent;
    if(isInbox) {
        inboxTemplateContent =
            '<span>' + message.sender.username + ': </span>' +
            '<span class="name">' + message.text + '</span>'
            '<span class="badge pull-right">' + message.sentDatetime + '</span>';
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
            '<span>You: </span>' +
            '<span class="name">' + message.text + '</span>'
            '<span class="badge pull-right">' + message.sentDatetime + '</span>';
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
    } else {
        inbox_message_conv_id = current_conv_id;
    }
    var isNewConversation = true;
    $('#inbox a.list-group-item').each(function() {
        var conv_id = $(this).data("convid");
        if(inbox_message_conv_id == conv_id) {
            $(this).find('.inbox-content').replaceWith(inboxTemplateContent);
            isNewConversation = false;
        }
    });
    if(isNewConversation) {
         var inboxTemplate =
            '<a class="list-group-item list-group-item-success" ' +
                'data-convid="' + message.conversation.conversationId + '">' +
                '<span>' + message.conversation.user1.username + '</span>' +
                '<span> & </span>' +
                '<span class="name">' + message.conversation.user2.username + '</span>' +
                inboxTemplateContent +
            '</a>'
        $('#inbox').prepend(inboxTemplate);
    }
    if(current_conv_id == inbox_message_conv_id) {
        $("ul[class='chat']").prepend(messageTemplate);
    }


    if(!isInbox) {
        $("#msg-container").stop().animate({scrollTop: 0}, 1000);
    }
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
});