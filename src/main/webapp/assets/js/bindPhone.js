$(document).ready(function () {

    $('#bind-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var phone = data.phone;
        var phoneCaptcha = data.phone_captcha;
        if (!$.validatePhoneFormat(phone)) {
            $.msg('手机号格式错误');
            return;
        }
        if (phoneCaptcha.trim() === '') {
            $.msg('请输入验证码');
            return;
        }
        $.post('/user/modify/bindphone', data, function (data) {
            if (!data.status) {
                $.msg(data.msg);
            } else {
                $.msg('绑定成功');
            }
        });
    });

    var captchaInput = $('#input-captcha');
    var phone = $('#phone');

    $('#send-button').click(function () {
        var captcha = captchaInput.val();
        var number = phone.val();
        var that = $(this);
        that.prop('disabled', true);
        $.post('/captcha/phone', {captcha: captcha, phone: number}, function (data) {
            if (data.status) {
                $.msg('发送成功');
                var count = 59;
                var timer = setInterval(function () {
                    that.text(count-- + 's');
                    if (count === 0) {
                        that.text('重新获取');
                        that.removeProp('disabled');
                        clearInterval(timer);
                    }
                }, 1000);
            } else {
                $.msg('发送失败');
                updateCaptcha();
                that.removeProp('disabled');
            }
        }, function () {
            $.msg('网络异常');
            that.removeProp('disabled');
        });

    });

});
