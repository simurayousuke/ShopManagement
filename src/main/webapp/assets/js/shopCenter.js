$(document).ready(function () {
    $('#modify-shop-name').click(function () {
        bootbox.prompt("请输入新店铺名", function (result) {
            if (result === null) {
                return;
            }
            $.post('/shop/modify/name', {name: result}, function (data) {
                if (data.status) {
                    $.msg('修改成功');
                    location.reload();
                } else {
                    $.msg('修改失败');
                }
            }, function () {
                $.msg('网络异常');
            });
        });
    });
    $('#modify-shop-description').click(function () {
        bootbox.prompt({
            title: "请输入新店铺描述",
            inputType: "textarea",
            callback: function (result) {
                if (result === null) {
                    return;
                }
                $.post('/shop/modify/description', {description: result}, function (data) {
                    if (data.status) {
                        $.msg('修改成功');
                        location.reload();
                    } else {
                        $.msg('修改失败');
                    }
                }, function () {
                    $.msg('网络异常');
                });
            }
        });
    });
    $('#transfer-shop').click(function () {
        var pwd;
        bootbox.prompt({
            title: "请输入密码",
            inputType: "password",
            callback: function (result) {
                if (result === null) {
                    return;
                }
                pwd = result;
                bootbox.prompt("请输入对方用户名", function (result) {
                    if (result === null) {
                        return;
                    }
                    $.post('/shop/modify/transfer', {username: result, pwd: pwd}, function (data) {
                        if (data.status) {
                            $.msg('转让成功');
                            location.reload();
                        } else {
                            $.msg('转让失败');
                        }
                    }, function () {
                        $.msg('网络异常');
                    });
                });
            }
        });
    });

});
