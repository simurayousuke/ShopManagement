$(document).ready(function () {
    $('.menu .nav').on('click', 'li:not(.nav-parent) > a', function () {
        var $this = $(this);
        $('.menu .nav .active').removeClass('active');
        $this.closest('li').addClass('active');
        $this.closest('.nav-parent').addClass('active');
    });
});
