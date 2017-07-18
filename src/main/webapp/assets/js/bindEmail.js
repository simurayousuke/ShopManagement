$(document).ready(function () {

    $('.bind-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var email = data.email;
        var captcha = data.captcha;
        if (!$.validateEmailFormat(email)) {
            $.msg('邮箱格式错误');
            return;
        }
        if (captcha.length !== 4) {
            $.msg('请输入验证码');
            return;
        }
        $.post('/user/modify/bindemail', data, function (data) {
            if (!data.status) {
                $.msg(data.msg);
            } else {
                $.msg('已发送邮件，请前往邮箱查看。');
            }
        });
    });

});
