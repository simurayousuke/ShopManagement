#sql("findById")
SELECT * FROM t_good WHERE id = #para(0);
#end
#sql("findByUuid")
SELECT * FROM t_good WHERE uuid = #para(0);
#end
#sql("findByShopId")
SELECT * FROM t_good WHERE shop_id = #para(0) AND status = 1 ORDER BY id;
#end
