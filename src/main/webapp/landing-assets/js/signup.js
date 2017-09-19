$(document).ready(function () {
    var name = "CSRF-TOKEN=";
    var ca = document.cookie.split(';');
    var csrf = "";
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1);
        if (c.indexOf(name) != -1) {
            csrf = c.substring(name.length, c.length);
            break;
        }
    }
    $("#csrf").val(csrf);
    $("#btn").click();
})

