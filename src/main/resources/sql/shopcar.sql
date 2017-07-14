#sql("findByUserId")
SELECT * FROM t_shop_car WHERE user_id = #para(0);
#end
#sql("findByUserIdAndGoodId")
SELECT * FROM t_shop_car WHERE user_id = #para(0) AND good_id = #para(1);
#end