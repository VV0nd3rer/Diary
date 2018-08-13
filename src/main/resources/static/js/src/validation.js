/**
 * Created by Kverchi on 13.8.2018.
 */
function checkDataListInput(inputField) {
    var inputVal = inputField.val();
    var hiddenInput = $("#" + inputField.attr("id") + '-hidden');
    var res = false;
    if (inputVal === '') {
        hiddenInput.val('');
        return res;
    }
    console.log("checking data list input...");

    var datalistField = inputField.attr('list');
    var datalistOptions = $('#' + datalistField + ' option');

    var validationMessage = $(".datalistValidationMessage");
    res = isDatalistInputValid(datalistOptions, inputVal, hiddenInput);
    if (res) {
        validationMessage.hide();
    }
    else {
        validationMessage.show();
    }
    return res;
}
function isDatalistInputValid(datalistOptions, inputVal, hiddenInput) {
    var res = false;
    for (var i = 0; i < datalistOptions.length; i++) {
        var option = datalistOptions[i];
        if (option.innerText === inputVal) {
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
function checkMinMaxLength(o, n, min, max ) {
    if ( o.val().length > max || o.val().length < min ) {

        addErrMsgToField( "Length of " + n + " must be between " +
            min + " and " + max + ".", o);
        return false;
    }/* else {
     remErrMsg();
     return true;
     }*/
    return true;
}
function addErrMsgToField( text, object ) {
    tips
        .text( text )
        .addClass( "alert alert-danger" );
    object.addClass( "alert alert-danger" );
    setTimeout(function() {
        object.removeClass("alert alert-danger", 1500);
        tips.removeClass( "alert alert-danger", 1500 );
        tips.empty();
    }, 500 );
}