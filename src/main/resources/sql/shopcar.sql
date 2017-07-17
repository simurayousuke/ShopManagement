#sql("findByUserId")
SELECT * FROM t_shop_car WHERE user_id = #para(0);
#end
#sql("findByUserIdAndGoodId")
SELECT * FROM t_shop_car WHERE user_id = #para(0) AND good_id = #para(1);
#end
#sql("findRecordByUserId")
SELECT t_shop_car.id,
t_shop_car."count",
t_shop.shop_name,
t_shop.uuid as shop_uuid,
t_good.uuid as good_uuid,
t_good.good_name,
t_good.avator,
t_good.price,
t_good.number
FROM t_shop_car
 INNER JOIN t_shop ON t_shop_car.shop_id = t_shop.id
 INNER JOIN t_good ON t_shop_car.good_id = t_good.id
 WHERE t_shop_car.user_id = #para(0);
#end