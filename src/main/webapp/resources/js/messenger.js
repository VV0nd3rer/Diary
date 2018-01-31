/**
 * Created by Liudmyla Melnychuk on 17.1.2018.
 */
$(document).ready(function(){
 $("a.list-group-item").click(function(e) {
     e.preventDefault();
     console.log("inbox div clicked!");
     var id = $(this).find(".conversationId").text();
     var conversation_url = "/messages/conversation/" + id;
     console.log(conversation_url);
     $.get("/messages/conversation/" + id, function (data) {
         $("#inbox").replaceWith(data);
     });
 })
});
