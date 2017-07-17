# 购物车

<ol>
<li>添加商品

请求地址：/user/shopcar/add

参数:

* uuid 商品的uuid
* count 商品的数量

</li>
<li>修改商品数量

请求地址：/user/shopcar/modify

参数:

* id shopcar的id
* count 商品的数量


</li>
<li>删除商品

请求地址：/user/shopcar/del

参数:

* id shopcar的id

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
