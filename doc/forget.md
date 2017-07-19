# 找回密码

<ol>
<li>验证手机及验证码，返回激活码（code）

请求地址：/forget/validatePhone

参数:

* phone 手机号
* captcha 短信验证码

</li>
<li>向邮箱发送验证邮件

请求地址：/forget/validateEmail

参数:

* email 邮箱地址

</li>
<li>重置密码

请求地址：/forget/reset

参数:

* code 手机或邮箱激活码
* pwd 新密码

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
