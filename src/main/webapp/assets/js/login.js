$(document).ready(function () {

    var captchaImg = $('.captcha');

    var updateCaptcha = function () {
        captchaImg.prop('src', '/captcha/image?v=' + Math.random());
    };

    var captchaInput = $('#captcha');

    $('#send-button').click(function () {
        var captcha = captchaInput.val();
        $.post('/captcha/phone', {captcha: captcha}, function (data) {
            if (data.status) {
                $.msg('发送成功');
            } else {
                $.msg('发送失败');
            }
        });
    });

    updateCaptcha();

    captchaImg.click(function () {
        updateCaptcha();
    });

    $("#username-form").submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var username = data.username;
        var pwd = data.pwd;
        var captcha = data.captcha;
        if (username.length < 1 || username.length > 20) {
            $.msg('用户名长度应为1-20位');
            return;
        }
        if (pwd.length < 6 || pwd.length > 32) {
            $.msg('密码长度应为6-32位');
            return;
        }
        if (captcha.length !== 4) {
            $.msg('验证码长度应为4位');
            return;
        }
        $.post('/login/username', data, function (data) {
            if (data.status) {
                location.href = '/';
            } else {
                $.msg('用户名或密码错误');
            }
        });
    });

    $('#email-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var email = data.email;
        var pwd = data.pwd;
        var captcha = data.captcha;
        if (!$.validateEmailFormat(email)) {
            $.msg('邮箱格式错误');
            return;
        }
        if (pwd.length < 6 || pwd.length > 32) {
            $.msg('密码长度应为6-32位');
            return;
        }
        if (captcha.length !== 4) {
            $.msg('验证码长度应为4位');
            return;
        }
        $.post('/login/email', data, function (data) {
            if (data.status) {
                location.href = '/';
            } else {
                $.msg('邮箱或密码错误');
            }
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
        $.post('/login/phone', data, function (data) {
            if (data.status) {
                location.href = '/';
            } else {
                $.msg('验证码错误');
            }
        });
    });

});
