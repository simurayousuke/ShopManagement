# 修改店铺信息

<ol>
<li>修改店铺名称

请求地址：/shop/modify/name

参数:

* name 店铺名称

</li>
<li>修改店铺描述

请求地址：/shop/modify/description

参数:

* description 店铺描述


</li>
<li>转让店铺

请求地址：/shop/modify/transfer

参数:

* username 目标用户名
* pwd 密码 6-32位

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
