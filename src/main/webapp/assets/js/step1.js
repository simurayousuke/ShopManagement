$(document).ready(function () {
    $('#send-button').click(function () {
        var captcha = captchaInput.val();
        var that = this;
        that.prop('disabled', true);
        $.post('/captcha/phone', {captcha: captcha}, function (data) {
            if (data.status) {
                $.msg('发送成功');
                var count = 59;
                var timer = setInterval(function () {
                    that.prop('value', count-- + 's');
                    if (count === 0) {
                        that.prop('value', '重新获取');
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
