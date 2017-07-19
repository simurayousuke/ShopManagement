$(document).ready(function () {

    $('.comment-button').click(function () {
        var that = $(this);
        var order = that.parent().parent().parent().find('.order-number')[0].innerText.substr(4);
        var id = that.data().goodid;
        comment(order, id);
    });

    function comment(order, id) {
        $.form("<div>" +
            "<label class=\"radio-inline\">" +
            " <input type=\"radio\" id=\"good-cmt\" name=\"positive\"> 好评" +
            "  </label>" +
            "  <label class=\"radio-inline\">" +
            "  <input type=\"radio\" name=\"negative\">差评" +
            "  </label>" +
            " </div>" +
            " <div>" +
            " <textarea id='cmt-text' style='resize: none' class=\"form-control\" placeholder=\"评论\"></textarea>" +
            " </div>", "评论", "var context=$('#cmt-text').val();" +
            "var good=$('#good-cmt').attr('checked')?true:false;" +
            "var order='" + order + "';" +
            "var id=" + id + ";" +
            "$.post('/order/comment',{order:order,id:id,context:context,good:good},function(data){" +
            "if(data.status){" +
            "$.warn('评论成功！',function(){location.reload();});" +
            "}else{" +
            "$.msg(data.msg);" +
            "}" +
            "});");
    }

    $('.pay-button').click(function () {
        var that = $(this);
        var order = that.parent().find('span').text().substr(4);
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

    $('.refund-button').click(function () {
        var that = $(this);
        var order = that.parent().parent().parent().find('.order-number')[0].innerText.substr(4);
        var id = that.data().goodid;
        $.post('/order/refund', {order: order, id: id}, function (data) {
            if (data.status) {
                $.warn('申请成功！', function () {
                    location.reload();
                });
            } else {
                $.alert('错误', data.msg);
            }
        }, function () {
            $.alert('错误', '网络异常');
        });
    });

    $('.check-button').click(function () {
        var that = $(this);
        var order = that.parent().parent().parent().find('.order-number')[0].innerText.substr(4);
        var id = that.data().goodid;
        $.post('/order/check', {order: order, id: id}, function (data) {
            if (data.status) {
                $.warn('确认收货成功，记得评价哦！', function () {
                    location.reload();
                });
            } else {
                $.alert('错误', data.msg);
            }
        }, function () {
            $.alert('错误', '网络异常');
        });
    });

    $('.close-button').click(function () {
        var that = $(this);
        var order = that.parent().parent().parent().find('.order-number')[0].innerText.substr(4);
        var id = that.data().goodid;
        $.post('/order/close', {order: order, id: id}, function (data) {
            if (data.status) {
                $.warn('退款成功！', function () {
                    location.reload();
                });
            } else {
                $.alert('错误', data.msg);
            }
        }, function () {
            $.alert('错误', '网络异常');
        });
    });

    $('.send-button').click(function () {
        var that = $(this);
        var order = that.parent().parent().parent().find('.order-number')[0].innerText.substr(4);
        var id = that.data().goodid;
        $.post('/order/send', {order: order, id: id}, function (data) {
            if (data.status) {
                $.warn('发货成功！', function () {
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
