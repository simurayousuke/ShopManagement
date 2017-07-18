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

    $.alert = function (title, msg) {
        bootbox.alert({
            size: "small",
            title: title,
            message: msg
        })
    };

    $.warn = function (msg, callback) {
        bootbox.alert({
            size: "small",
            title: '警告',
            message: msg,
            callback: callback
        })
    };

    $.confirm = function (msg, buttonOK, buttonCancel, callback) {
        bootbox.confirm({
            size: "small",
            message: msg,
            buttons: {
                confirm: {
                    label: buttonOK
                },
                cancel: {
                    label: buttonCancel
                }
            },
            callback: callback
        });
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

    $.formatFloat = function (value, num) {
        var a, b, c, i;
        a = value.toString();
        b = a.indexOf(".");
        c = a.length;
        if (num === 0) {
            if (b !== -1) {
                a = a.substring(0, b);
            }
        } else {//如果没有小数点
            if (b === -1) {
                a = a + ".";
                for (i = 1; i <= num; i++) {
                    a = a + "0";
                }
            } else {//有小数点，超出位数自动截取，否则补0
                a = a.substring(0, b + num + 1);
                for (i = c; i <= b + num; i++) {
                    a = a + "0";
                }
            }
        }
        return a;
    };

    /**
     * @return {string}
     */
    $.getPara = function GetQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r !== null) {
            return decodeURIComponent(r[2]);
        }
        return "";
    };


    var emailPattern = /^['_A-Za-z0-9-]+(\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$/;
    var phonePattern = /^1[34578][0-9]{9}$/;
    var pricePattern = /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))|0+$/;
    var positiveIntPattern = /^\d+$/;

    $.validateEmailFormat = function (email) {
        return emailPattern.test(email);
    };

    $.validatePhoneFormat = function (phone) {
        return phonePattern.test(phone);
    };

    $.validatePriceFormat = function (price) {
        return pricePattern.test(price);
    };

    $.validatePositiveIntFormat = function (number) {
        return positiveIntPattern.test(number);
    }

})(jQuery);
