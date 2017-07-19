$(document).ready(function () {

    $('#step2-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var pwd = data.pwd;
        if (pwd.length < 6 || pwd.length > 32) {
            $.msg('密码长度应为6-32位');
            return;
        }
        if ($('#check').val() !== pwd) {
            $.msg('两次输入密码不一致');
            return;
        }
        $.post('/forget/doreset', data, function (data) {
            if (data.status) {
                $.warn('修改成功！', function () {
                    location.href = "/login";
                });
            } else {
                $.alert('警告', data.msg);
            }
        }, function () {
            $.alert('警告', '网络异常');
        });
    });

});
