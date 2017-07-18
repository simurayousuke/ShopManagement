$(document).ready(function () {
    $('#charge-button').click(function () {
        var that = $(this);
        var input = $('#charge-value');
        var value = input.val();
        if (!$.validatePriceFormat(value)) {
            $.alert('警告', '格式不正确');
            return;
        }
        that.prop('disabled', true);
        $.post('/user/modify/charge', {value: value}, function (data) {
            if (data.status) {
                $.warn('充值成功！', function () {
                    input.val('');
                    location.reload();
                });
            } else {
                $.alert('警告', data.msg);
                that.removeProp('disabled');
            }
        }, function () {
            $.alert('警告', '网络异常');
            that.removeProp('disabled');
        });
    });
});
