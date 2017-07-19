$(document).ready(function () {

    var repeat = $('#repeat-pwd');

    $('#pwd-form').submit(function (e) {
        e.preventDefault();
        var $this = $(this);
        var data = $this.serializeObject();
        var oldPwd = data.old_pwd;
        var newPwd = data.new_pwd;
        var repeatPwd = repeat.val();
        if (oldPwd.trim() === '') {
            $.msg('请输入旧密码');
            return;
        }
        if (newPwd.trim() === '') {
            $.msg('请输入新密码');
            return;
        }
        if (newPwd !== repeatPwd) {
            $.msg('两次密码不一致');
            return;
        }
        $.post('/user/modify/changepwd', data, function (data) {
            if (!data.status) {
                $.msg(data.msg);
            } else {
                $.msg('修改成功');
                $this[0].reset();
            }
        });
    });

});
