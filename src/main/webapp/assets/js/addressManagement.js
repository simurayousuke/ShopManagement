$(document).ready(function () {

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
        if (dist.trim() === '') {
            $.msg('请输入想信息');
            return;
        }
        var data = {
            address: province + city + dist + detail,
            reciver_name: name,
            phone: phone
        };
        $.post('', data, function (data) {
            if (!data.status) {
                $.msg(data.msg);
            } else {
                $.msg('添加成功');
                form[0].reset();
            }
        });
    });

});
