$(document).ready(function () {


    $('#search-button').click(function () {
        location.href = "/search/" + $('#search-word').val();
    });

});
