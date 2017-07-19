#sql("findById")
SELECT * FROM t_comment WHERE id = #para(0);
#end
#sql("findByGoodId")
SELECT * FROM t_comment WHERE good_id = #para(0) ORDER BY id;
#end
#sql("findByShopId")
SELECT * FROM t_comment WHERE shop_id = #para(0) ORDER BY id;
#end
#sql("findByUserId")
SELECT * FROM t_comment WHERE buyer_id = #para(0) ORDER BY id;
#end
#sql("findByGoodIdAndGood")
SELECT * FROM t_comment WHERE good_id = #para(0) AND is_good = 1 ORDER BY id;
#end
#sql("findByShopIdAndGood")
SELECT * FROM t_comment WHERE shop_id = #para(0) AND is_good = 1 ORDER BY id;
#end
#sql("findByGoodIdAndBad")
SELECT * FROM t_comment WHERE good_id = #para(0) AND is_good = 0 ORDER BY id;
#end
#sql("findByShopIdAndBad")
SELECT * FROM t_comment WHERE shop_id = #para(0) AND is_good = 0 ORDER BY id;
#end
#sql("findRecordByGoodId")
SELECT username, is_good, description, comment_time FROM t_comment INNER JOIN t_user ON buyer_id = t_user.id WHERE good_id = #para(0);
#end