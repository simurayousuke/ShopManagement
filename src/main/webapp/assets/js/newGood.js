/**
 * Created by forre on 2017/7/13.
 */

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
});

