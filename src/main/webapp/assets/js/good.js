$(document).ready(function () {

    var uuid = location.pathname;
    uuid = uuid.substr(uuid.lastIndexOf('/') + 1);

    var countInput = $('#good-number');

    $('#count-dec').click(function () {
        var count = countInput.val();
        if (count >= 2) {
            countInput.val(--count);
        }
    });

    $('#count-inc').click(function () {
        var count = countInput.val();
        countInput.val(++count);
    });

    countInput.change(function () {
        var count = countInput.val();
        if (count <= 0 || isNaN(count)) {
            countInput.val(1);
        }
    });

    $('#add-to-shopcar').click(function () {
        $.post('/user/shopcar/add', {uuid: uuid, count: countInput.val()}, function (data) {
            if (data.status) {
                $.confirm('添加成功', '前往购物车', '继续购物', function (result) {
                    if (result) {
                        location.href = "/user/shopcar";
                    }
                });
            } else {
                $.alert('Error', '添加失败');
            }
        }, function () {
            $.alert('Error', '网络异常');
        });
    });

});