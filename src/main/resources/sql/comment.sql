#sql("findById")
SELECT * FROM t_comment WHERE id = #para(0);
#end
#sql("findByGoodId")
SELECT * FROM t_comment WHERE good_id = #para(0);
#end
#sql("findByShopId")
SELECT * FROM t_comment WHERE shop_id = #para(0);
#end
#sql("findByUserId")
SELECT * FROM t_comment WHERE buyer_id = #para(0);
#end
#sql("findByGoodIdAndGood")
SELECT * FROM t_comment WHERE good_id = #para(0) AND is_good = 1;
#end
#sql("findByShopIdAndGood")
SELECT * FROM t_comment WHERE shop_id = #para(0) AND is_good = 1;
#end
#sql("findByGoodIdAndBad")
SELECT * FROM t_comment WHERE good_id = #para(0) AND is_good = 0;
#end
#sql("findByShopIdAndBad")
SELECT * FROM t_comment WHERE shop_id = #para(0) AND is_good = 0;
#end