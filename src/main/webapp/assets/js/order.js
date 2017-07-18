$('#comment-button').click(function () {

    $('.pay-button').click(function () {
        var that = $(this);
        var order = that.parent().find('span').innerText.substr(4);
        $.post('/order/pay', {order: order}, function (data) {
            if (data.status) {
                $.warn('支付成功！', function () {
                    location.reload();
                });
            } else {
                $.alert('错误', data.msg);
            }
        }, function () {
            $.alert('错误', '网络异常');
        });
    });

});
