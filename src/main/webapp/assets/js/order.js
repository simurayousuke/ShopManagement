$(document).ready(function () {

    $('.comment-button').click(function () {
        var that = $(this);
        var order = that.parent().parent().parent().find('.order-number')[0].innerText.substr(4);
        var id = that.data().goodid;
        comment(order, id);
    });

    function comment(order, id) {
        $.form("<div>" +
            "<label class=\"radio-inline\">" +
            " <input type=\"radio\" id=\"good-cmt\" name=\"positive\"> 好评" +
            "  </label>" +
            "  <label class=\"radio-inline\">" +
            "  <input type=\"radio\" name=\"negative\">差评" +
            "  </label>" +
            " </div>" +
            " <div>" +
            " <textarea id='cmt-text' class=\"form-control\" placeholder=\"评论\"></textarea>" +
            " </div>", "评论", "var context=$('#cmt-text').val();" +
            "var good=$('#good-cmt').attr('checked')?true:false;" +
            "var order='" + order + "';" +
            "var id=" + id + ";" +
            "$.post('/order/comment',{order:order,id:id,context:context,good:good},function(data){" +
            "if(data.status){" +
            "$.warn('评论成功！',function(){location.reload();});" +
            "}else{" +
            "$.msg(data.msg);" +
            "}" +
            "});");
    }

});
