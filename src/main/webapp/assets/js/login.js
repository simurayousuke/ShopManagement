$(document).ready(function () {

    var captchaImg = $('.captcha');
    var phoneForm=$('phone-form');

    var updateCaptcha = function () {
        captchaImg.prop('src', '/captcha/image?v=' + Math.random());
    };

    var captchaInput = $('#captcha');


    $('#send-button').click(function () {
        var captcha = captchaInput.val();
        var number=phoneForm.serializeObject().phone;
        var that = $(this);
        that.prop('disabled', true);
        $.post('/captcha/phone', {captcha: captcha,phone:number}, function (data) {
            if (data.status) {
                $.msg('发送成功');
                var count = 59;
                var timer = setInterval(function () {
                    that.prop('value', count-- + 's');
                    if(count===0){
                        that.prop('value','重新获取');
                        that.removeProp('disabled');
                        clearInterval(timer);
                    }
                }, 1000);
            } else {
                $.msg('发送失败');
                updateCaptcha();
                that.removeProp('disabled');
            }
        },function(){
            $.msg('网络异常');
            that.removeProp('disabled');
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
                $.msg(data.msg);
                updateCaptcha();
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
                $.msg(data.msg);
                updateCaptcha();
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
                $.msg(data.msg);
                updateCaptcha();
            }
        });
    });

});
