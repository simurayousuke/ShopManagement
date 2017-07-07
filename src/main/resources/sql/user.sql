#sql("findByUsername")
SELECT * FROM t_user WHERE username = #para(0)
#end
#sql("findByPhoneNumber")
SELECT * FROM t_user WHERE phone = #para(0)
#end
#sql("findByEmail")
SELECT t_user.id AS id, username, salt, pwd, uuid, avator, phone FROM t_user RIGHT JOIN t_email ON t_email.user_id = t_user.id WHERE email = #para(0);
#end