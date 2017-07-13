KindEditor.create('textarea.kindeditor', {
    basePath: '//cdn.bootcss.com/zui/1.7.0/lib/kindeditor/',
    bodyClass: 'article-content',
    resizeType: 1,
    allowPreviewEmoticons: false,
    allowImageUpload: false,
    items: [
        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
        'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
        'insertunorderedlist', '|', 'emoticons'
    ]
});

$(document).ready(function () {
    var captchaImg = $('.captcha');

    var updateCaptcha = function () {
        captchaImg.prop('src', '/captcha/image?v=' + Math.random());
    };

    updateCaptcha();

    captchaImg.click(function () {
        updateCaptcha();
    });

    $('#create-good-button').click(function (e) {
        var form = $('#create-good-form');
        var data = form.serializeObject();
        var description = data.description = $("iframe").contents().find("body").html();
        var name = data.name;
        var price = data.price;
        var number = data.number;
        data.description = description;
        var captcha = $('#good-captcha').val();
        if (name.length < 1) {
            $.msg('请输入商品名');
            return;
        }
        if (description.length < 1) {
            $.msg('请输入商品描述');
            return;
        }
        if (!$.validatePriceFormat(price)) {
            $.msg('价格不正确');
            return;
        }
        if (!$.validatePositiveIntFormat(number)) {
            $.msg('库存不正确');
            return;
        }
        if (captcha.length !== 4) {
            $.msg('验证码长度应为4位');
            return;
        }
        $.post('/shop/good/add', data, function (data) {
            if (data.status) {
                location.href = '/shop/good';
            } else {
                $.msg(data.msg);
                updateCaptcha();
            }
        });
    });

});


