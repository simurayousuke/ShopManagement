#sql("findByUserId")
SELECT * FROM t_order_number WHERE user_id = #para(0);
#end
#sql("findByOrderNumber")
SELECT * FROM t_order_number WHERE order_number = #para(0);
#end
#sql("findByUserIdAndStatus")
SELECT * FROM t_order_number WHERE user_id = #para(0) AND status = #para(1);
#end