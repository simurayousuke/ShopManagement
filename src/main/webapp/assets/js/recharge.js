$(document).ready(function () {
    $('#charge-button').click(function () {
        $.post('/user/modify/charge', {value: $('#charge-value').val()}, function (data) {
            if (data.status) {
                $.warn('充值成功！', function () {
                    location.reload();
                });
            } else {
                $.alert('警告', data.msg);

            }
        }, function () {
            $.alert('警告', '网络异常');

        });
    });
});
