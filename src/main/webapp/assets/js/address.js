$(document).ready(function () {

    $('input[name="address"]').click(function () {
        var that = $(this);
        var parent = that.parent().parent().parent();
        var current = $('.admit-address');
        current.removeClass('admit-address');
        current.find('div[data-type="sent-to-icon"]').removeClass('show');
        parent.addClass('admit-address');
        parent.find('div[data-type="sent-to-icon"]').addClass('show');
    });

});