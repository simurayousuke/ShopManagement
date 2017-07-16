#sql("findById")
SELECT * FROM t_address WHERE id = #para(0);
#end
#sql("findByUserId")
SELECT * FROM t_address WHERE user_id = #para(0);
#end
#sql("findDefaultByUserId")
SELECT * FROM t_address WHERE user_id = #para(0) AND is_default = 1;
#end