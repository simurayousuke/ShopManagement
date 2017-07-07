$("#formLogin").submit(function (e) {
    e.preventDefault();
    var form = $(this);
    $.ajax({
        type: 'post',
        url: '',
        data: form.serialize()
    });
});
    
