$(document).ready(function () {

    var motoNum;

    function updateCount(input) {
        var id = input.dataset.id;
        var count = input.val();
        $.post('/user/shopcar/modify', {id: id}, function (data) {
            if (!data.status) {
                $.msg('修改数量失败');
            }
        }, function () {
            $.msg('网络异常');
        });
    }

    $('.del-button').click(function () {
        var that = $(this);
        var id = that.dataset.id;
        $.post('/user/shopcar/del', {id: id}, function (data) {
            if (data.status) {
                $.msg('删除成功');
            } else {
                $.msg('删除失败');
            }
        }, function () {
            $.msg('网络异常');
        });
    });

    $('.icon-plus').click(function () {
        var that = $(this);
        var countInput = that.parent().parent().find('input');
        var count = countInput.val();
        countInput.val(++count);
        updateCount(countInput);
    });

    $('.icon-minus').click(function () {
        var that = $(this);
        var countInput = that.parent().parent().find('input');
        var count = countInput.val();
        countInput.val(--count);
        updateCount(countInput);
    });

    $('.good-number').focus(function () {
        motoNum = $(this).val();
    });

    $('.good-number').change(function () {
        var that = $(this);
        var count = that.val();
        if (count <= 0 || isNaN(count)) {
            that.val(motoNum);
        } else {
            updateCount(that);
        }
    });

});
