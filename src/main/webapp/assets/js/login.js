$(document).ready(function () {

    $("#username-form").submit(function (e) {
        e.preventDefault();
        var form = $(this);
        post('/login/username', form.serialize(), function (data) {
            if (data.status) {
                location.href = '/';
            } else {
                new $.zui.Messager('用户名或密码错误', {
                    close: false // 禁用关闭按钮
                }).show();
            }
        });
    });

    $('#email-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        post('/login/email', form.serialize(), function (data) {
            if (data.status) {
                location.href = '/';
            } else {
                new $.zui.Messager('邮箱或密码错误', {
                    close: false // 禁用关闭按钮
                }).show();
            }
        });
    });

    $('#phone-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        post('/login/phone', form.serialize(), function (data) {
            if (data.status) {
                location.href = '/';
            } else {
                new $.zui.Messager('验证码错误', {
                    close: false // 禁用关闭按钮
                }).show();
            }
        });
    });

    var post = function (url, data, success) {
        $.ajax({
            type: 'post',
            url: url,
            data: data,
            success: success,
            error: function () {
                new $.zui.Messager('网络错误', {
                    close: false // 禁用关闭按钮
                }).show();
            }
        });
    };

});
