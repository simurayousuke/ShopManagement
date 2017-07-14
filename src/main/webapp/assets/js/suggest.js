$.post('/suggest/黄', {}, function (data) {
    if (data.status) {
        var json = JSON.parse(data.suggestions);
        for (var i = 0; i < json.length; ++i) {
            console.log(json[i].suggestion);
        }
    } else {
        console.log('发送失败');
    }
}, function () {
    console.log('网络异常');
});