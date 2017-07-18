$(document).ready(function () {
    $('#upload').uploader({
        autoUpload: true,
        url: '/upload/user',
        responseHandler: function (responseObject) {
            var data = JSON.parse(responseObject.response);
            if (!data.status) {
                return data.msg;
            } else {
                $('#create-user-avator').val(data.url);
                $('.user-photo').prop('src', '//smcdn.yangzhizhuang.net/' + data.url);
                var files = $('.file');
                for (var i = 0; i < files.length; ++i) {
                    files[i].style.display = 'none';
                }
            }
        },
        filters: {
            mime_types: [
                {
                    title: '图片',
                    extensions: 'jpg,gif,png'
                }
            ],
            max_file_size: '5mb'
        }
    });

    $('#set-user-photo').click(function () {
        $.post('/user/avator', {avator: $('#create-user-avator').val()}, function (data) {
            if (data.status) {
                $.warn('上传成功', function () {
                    location.reload();
                });
            } else {
                $.alert('警告', '上传失败');

            }
        }, function () {
            $.alert('警告', '网络异常');

        });
    });
});
