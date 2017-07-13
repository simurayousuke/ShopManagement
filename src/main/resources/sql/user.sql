#sql("findByUsername")
SELECT * FROM t_user WHERE username = #para(0)
#end
#sql("findByPhoneNumber")
SELECT * FROM t_user WHERE phone = #para(0)
#end
#sql("findByPhone")
SELECT * FROM t_user WHERE phone = #para(0)
#end
#sql("findByEmail")
SELECT * FROM t_user WHERE email = #para(0);
#end
#sql("findById")
SELECT * FROM t_user WHERE id = #para(0);
#end