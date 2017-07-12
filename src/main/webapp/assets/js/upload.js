$(document).ready(function () {
    $('#upload').uploader({
        autoUpload: true,
        url: '/upload',
        responseHandler: function (responseObject) {
            var data = JSON.parse(responseObject.response);
            if (!data.status) {
                return data.msg;
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
});
