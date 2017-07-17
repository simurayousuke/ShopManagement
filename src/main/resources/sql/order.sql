#sql("findByShopId")
SELECT * FROM t_order WHERE shop_id = #para(0);
#end
#sql("findByUserId")
SELECT * FROM t_order WHERE user_id = #para(0);
#end
#sql("findByOwnerId")
SELECT * FROM t_order WHERE owner_id = #para(0);
#end
#sql("findByOrderNumber")
SELECT * FROM t_order WHERE order_number = #para(0);
#end
#sql("findByOrderNumberAndGoodId")
SELECT * FROM t_order WHERE order_number = #para(0) AND good_id = #para(1);
#end
#sql("findByUserIdAndStatus")
SELECT * FROM t_order WHERE user_id = #para(0) AND order_status = #para(1);
#end
#sql("findByOwnerIdAndStatus")
SELECT * FROM t_order WHERE owner_id = #para(0) AND order_status = #para(1);
#end
#sql("findCheckedByUserId")
SELECT * FROM t_order WHERE user_id = #para(0) AND ( order_status = 4 OR order_status = 3 );
#end
#sql("findCheckedByOwnerId")
SELECT * FROM t_order WHERE owner_id = #para(0) AND ( order_status = 4 OR order_status = 3 );
#end