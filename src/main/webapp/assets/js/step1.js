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

    captchaImg.click(function () {
        updateCaptcha();
    });

});
