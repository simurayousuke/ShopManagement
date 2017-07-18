$(document).ready(function () {

    $('.del-button').click(function () {
        var that = $(this);
        var uuid = that.data().uuid;
        $.post('shop/good/remove', {uuid: uuid}, function (data) {
            if (data.status) {
                location.reload();
            } else {
                $.msg(data.msg);
            }
        });
    });

});