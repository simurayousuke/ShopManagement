$(document).ready(function () {

    var captchaImg = $('.captcha');

    var updateCaptcha = function () {
        captchaImg.prop('src', '/captcha/image?v=' + Math.random());
    };

    updateCaptcha();

    captchaImg.click(function () {
        updateCaptcha();
    });

    $("#create-shop-form").submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var name = data.name;
        var description = data.description;
        var captcha = data.captcha;
        if (name.length < 1 ) {
            $.msg('请输入店铺名');
            return;
        }
        if (description.length < 1) {
            $.msg('请输入店铺描述');
            return;
        }
        if (captcha.length !== 4) {
            $.msg('验证码长度应为4位');
            return;
        }
        $.post('/shop/modify/add', data, function (data) {
            if (data.status) {
                location.href = '/shop/center';
            } else {
                $.msg(data.msg);
                updateCaptcha();
            }
        });
    });
});
