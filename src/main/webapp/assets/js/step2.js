$(document).ready(function () {

    var code = location.pathname;
    code = code.substr(code.lastIndexOf('/') + 1);
    $('#activeCode').val(code);

    $('#step2-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var data = form.serializeObject();
        var username = data.username;
        var pwd = data.pwd;
        var check = $('#check').val();
        if (username.length < 1 || username.length > 20) {
            $.msg('用户名长度应为1-20位');
            return;
        }
        if (pwd.length < 6 || pwd.length > 32) {
            $.msg('密码长度应为6-32位');
            return;
        }
        if (pwd !== check) {
            $.msg('两次输入密码不一致');
            return;
        }
        $.post('/register/step2handler', data, function (data) {
            if (data.status) {
                location.href = '/register/success/';
            } else {
                $.msg(data.msg);
            }
        });
    });

});
