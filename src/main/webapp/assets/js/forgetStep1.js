$(document).ready(function () {

    var captchaImg = $('.captcha');
    var phone = $('#phone');

    var updateCaptcha = function () {
        captchaImg.prop('src', 'captcha/image?v=' + Math.random());
    };

    var captchaInput = $('#input-captcha-phone');
    updateCaptcha();

    captchaImg.click(function () {
        updateCaptcha();
    });

    $('#email-form').submit(function (e) {
        e.preventDefault();
        var $this = $(this);
        var data = $this.serializeObject();
        var email = data.email;
        var captcha = data.captcha;
        if (!$.validateEmailFormat(email)) {
            $.msg('邮箱格式错误');
            return;
        }
        if (captcha.trim().length !== 4) {
            $.msg('请输入验证码');
            return;
        }
        $.post('/forget/validateEmail', data, function (data) {
            if (!data.status) {
                $.msg(data.msg);
            } else {
                $.msg('邮件发送成功,请查收');
            }
        });
    });

    $('#send-button').click(function () {
        var captcha = captchaInput.val();
        var number = phone.val();
        var that = $(this);
        if (!$.validatePhoneFormat(number)) {
            $.msg('手机号格式错误');
            return;
        }
        that.prop('disabled', true);
        $.post('/captcha/phone', {captcha: captcha, phone: number}, function (data) {
            if (!data.status) {
                $.msg(data.msg);
                updateCaptcha();
                that.removeProp('disabled');
                return;
            }
            $.msg('发送成功');
            var count = 59;
            var timer = setInterval(function () {
                that.prop('value', count-- + 's');
                if (0 === count) {
                    that.prop('value', '重新获取');
                    that.removeProp('disabled');
                    clearInterval(timer);
                }
            }, 1000);
        }, function () {
            $.msg('网络异常');
            that.removeProp('disabled');
        });

    });

    $('#phone-form').submit(function (e) {
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
        $.post('/forget/validatePhone', data, function (data) {
            if (data.status) {
                location.href = '/forget/reset/' + data.code;
            } else {
                $.msg(data.msg);
                updateCaptcha();
            }
        });
    });

});
