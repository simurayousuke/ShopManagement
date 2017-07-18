# 用户信息相关

<ol>
<li>设置头像

请求地址：/user/modify/avator

参数:

* avator 头像key

</li>
<li>添加收货地址

请求地址：/user/modify/address

参数:

* name 收货人姓名
* phone 收货人手机号
* address 收货地址

</li>
<li>设置默认收货地址

请求地址：/user/modify/defaultaddress

参数:

* id 收货地址id

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
