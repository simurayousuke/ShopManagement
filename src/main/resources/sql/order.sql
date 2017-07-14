#sql("findByShopId")
SELECT * FROM t_order WHERE shop_id = #para(0);
#end
#sql("findByUserId")
SELECT * FROM t_order WHERE user_id = #para(0);
#end