/**
 * Created by forre on 2017/7/7.
 */

$("#formLogin").submit(function (e) {
    form = $(this);
    $.ajax({
        type: 'post'
        , url: ""
        , data: form.serialize()
        ,
    });
});
    
