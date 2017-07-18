$(document).ready(function () {

    var captchaImg = $('.captcha');

    var updateCaptcha = function () {
        captchaImg.prop('src', '/captcha/image?v=' + Math.random());
    };

    updateCaptcha();

    captchaImg.click(function () {
        updateCaptcha();
    });

});
