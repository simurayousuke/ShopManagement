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
        var description = data.description = $("iframe").contents().find("body").text();
        var name = data.name;
        var price = data.price;
        var number = data.number;
        data.description = description;
        var captcha = $('#good-captcha').val();
        data.captcha = captcha;
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

    $('#upload').uploader({
        autoUpload: true,
        url: '/upload/good',
        responseHandler: function (responseObject) {
            var data = JSON.parse(responseObject.response);
            if (!data.status) {
                return data.msg;
            } else {
                $('#create-good-avator').val(data.url);
                $('.good-photo').prop('src', '//smcdn.yangzhizhuang.net/' + data.url);
                var files = $('.file');
                for (var i = 0; i < files.length; ++i) {
                    files[i].style.display = 'none';
                }
            }
        },
        filters: {
            mime_types: [
                {
                    title: '图片',
                    extensions: 'jpg,gif,png'
                }
            ],
            max_file_size: '5mb'
        }
    });

});


