$(document).ready(function () {

    var input = $('#search-word');
    var suggestion = $('#search-suggestion');

    input.blur(function () {
        setTimeout(hideSuggest, 100);
    });
    input.keyup(function (event) {
        var x = event.keyCode || event.which;
        console.log(x);
        if (x == 13) {
            $('#search-button').click();
        }


        $.post('/suggest/' + $('#search-word').val(), {}, function (data) {
            if (data.status) {
                var json = JSON.parse(data.suggestions);
                if (json.length <= 0) {
                    hideSuggest();
                } else {
                    suggestion.empty();
                    for (var i = 0; i < json.length; ++i) {
                        suggestion.append('<li>' + json[i].suggestion + '</li>');
                        console.log(json[i].suggestion);
                    }
                    $('#gov_search_suggest').css('display', 'block');
                }
            } else {
                hideSuggest();
                console.log('发送失败');
            }
        }, function () {
            console.log('网络异常');
        })

    });
    function hideSuggest() {
        $('#gov_search_suggest').css('display', 'none');
    }







});