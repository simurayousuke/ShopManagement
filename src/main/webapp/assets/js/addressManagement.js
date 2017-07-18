$(document).ready(function () {

    $('.set-default-address').click(function () {
        var that = $(this);
        var id = that.data().addressid;
        $.post('/user/modify/defaultaddress', {id: id}, function (data) {
            if (!data.status) {
                $.alert("错误", data.msg);
            } else {
                $.msg("设置成功");
                location.reload();
            }
        });

    });

    $('#create-address-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var formData = form.serializeObject();
        var province = formData.province;
        var city = formData.city;
        var dist = formData.dist;
        var detail = formData.detail;
        var phone = formData.phone;
        var name = formData.name;
        if (!$.validatePhoneFormat(phone)) {
            $.msg('手机号格式错误');
            return;
        }
        if (name.trim() === '') {
            $.msg('请输入收货人姓名');
            return;
        }
        if (province.trim() === '') {
            $.msg('请输入省');
            return;
        }
        if (city.trim() === '') {
            $.msg('请输入市');
            return;
        }
        if (dist.trim() === '') {
            $.msg('请输入区');
            return;
        }
        if (detail.trim() === '') {
            $.msg('请输入详细信息');
            return;
        }
        var data = {
            address: province + city + dist + detail,
            name: name,
            phone: phone
        };
        var button = $('#new-address-button');
        button.prop('disabled', true);
        $.post('/user/modify/addaddress', data, function (data) {
            if (!data.status) {
                $.msg(data.msg);
                button.removeProp('disabled');
            } else {
                $('form').reset();
                location.reload();
            }
        });
    });

});
