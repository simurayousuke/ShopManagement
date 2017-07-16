/**
 * Created by forre on 2017/7/16.
 */
$('.menu .nav').on('click', 'li:not(.nav-parent) > a', function () {
    var $this = $(this);
    $('.menu .nav .active').removeClass('active');
    $this.closest('li').addClass('active');
    $this.closest('.nav-parent').addClass('active');
});