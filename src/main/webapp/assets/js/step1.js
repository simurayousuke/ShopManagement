$(document).ready(function () {

    var captchaImg = $('.captcha');
    var phone=$('#phone');

    var updateCaptcha = function () {
        captchaImg.prop('src', '/captcha/image?v=' + Math.random());
    };

    var captchaInput = $('#input-captcha-phone');
    updateCaptcha();

    $('[data-tab]').on('show.zui.tab', function(e) {
        var target=$(this).attr("data-target");
        if(target==='#register-email'){
            captchaInput = $('#input-captcha-email');
        }else if(target==='#register-phone'){
            captchaInput = $('#input-captcha-phone');
        }
    });

    $('#send-button').click(function () {
        var captcha = captchaInput.val();
        var number=phone.val();
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

    $('#email-form').submit(function (e) {
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
            $.msg('验证码长度应为4位');
            return;
        }
        $.post('/register/email', data, function (data) {
            if (data.status) {
                $.msg('邮件已发送')
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
        $.post('/register/phone', data, function (data) {
            if (data.status) {
                location.href = 'register/step2/'+data.activeCode;
            } else {
                $.msg(data.msg);
                updateCaptcha();
            }
        });
    });

    captchaImg.click(function () {
        updateCaptcha();
    });

});
