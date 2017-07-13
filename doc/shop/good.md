# 修改店铺信息

<ol>
<li>上架商品

请求地址：/shop/good/add

参数:

* name 商品名称
* description 商品描述
* price 商品价格
* number 商品库存
* avator 图片地址

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
