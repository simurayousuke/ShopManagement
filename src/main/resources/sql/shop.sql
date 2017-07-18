#sql("findByOwnerUserId")
SELECT * FROM t_shop WHERE owner_user_id = #para(0);
#end
#sql("findByShopName")
SELECT * FROM t_shop WHERE shop_name = #para(0);
#end
#sql("findById")
SELECT * FROM t_shop WHERE id = #para(0);
#end
#sql("findByUuid")
SELECT * FROM t_shop WHERE uuid = #para(0);
#end