$('#comment-button').click(function () {


    $.prompt("请输入评论", "text", function (result) {
        if (result === null) {
            return;
        }
        $.post('', {comment: result}, function (data) {
            if (data.status) {
                $.msg('评论成功');
                location.reload();
            } else {
                $.msg('评论失败');
            }
        });
    });
});
