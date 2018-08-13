function showModalDialog() {
    $("#modal-form").modal();
}
function hideModalDialog() {
    console.log('hiding modal dialog...')
    $("#modal-form").modal("hide");
    $(':input','#modal-form')
        .not(':button, :submit, :reset, :hidden')
        .val('')
        .removeAttr('checked')
        .removeAttr('selected');
    $(':input[type=hidden]','#modal-form').val('0');
    console.log('hided modal dialog');
}