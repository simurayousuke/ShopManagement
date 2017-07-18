# 订单相关

<ol>
<li>从购物车创建订单

请求地址：/order/create

参数:

* address 收货地址的id

</li>
<li>支付，直接从余额扣款

请求地址：/order/pay

参数:

* order 订单号

</li>
<li>申请退款

请求地址：/order/refund

参数:

* order 订单号
* id 商品id

</li>
<li>确认收货

请求地址：/order/check

参数:

* order 订单号
* id 商品id

</li>
<li>评论商品

请求地址：/order/comment

参数:

* order 订单号
* id 商品id
* context 评论内容
* good 是否好评（bool）

</li>
</ol>

* 返回值:

成功:
```json
{
  "status": true
}
```

失败:
```json
{
  "status": false,
  "msg": ""
}
```
