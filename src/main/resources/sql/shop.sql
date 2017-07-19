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
#sql("countByShopIdAndIsGood")
WITH comment_num
AS (
    SELECT
      count(*) AS num,
      shop_id
    FROM t_comment
    WHERE is_good = #para(0)
    GROUP BY shop_id
)
SELECT
  shop_name,
  coalesce(num, 0) AS good,
  username,
  t_shop.uuid,
  t_shop.avator
FROM t_shop LEFT JOIN comment_num ON shop_id = t_shop.id
  LEFT JOIN t_user ON t_user.id = t_shop.owner_user_id
#end