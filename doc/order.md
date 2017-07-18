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
