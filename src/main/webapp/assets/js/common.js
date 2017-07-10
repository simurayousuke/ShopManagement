(function ($) {

    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };

    $.msg = function (msg) {
        new $.zui.Messager(msg, {
            close: false
        }).show();
    };

    $.post = function (url, data, success, error) {

        error = error || function () {
                $.msg('网络异常');
            };

        $.ajax({
            type: 'post',
            url: url,
            data: data,
            success: success,
            error: error
        });

    };

    var emailPattern = /^['_A-Za-z0-9-]+(\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$/;
    var phonePattern = /^1[3|4|5|7|8][0-9]{9}$/;

    $.validateEmailFormat = function (email) {
        return emailPattern.test(email);
    };

    $.validatePhoneFormat = function (phone) {
        return phonePattern.test(phone);
    };

})(jQuery);
