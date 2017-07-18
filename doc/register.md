# 本项目提供两种注册方式,分为两步

## 第一步:

<ol>

<li>手机号注册

地址: /register/phone

参数:
* phone 手机号
* phone_captcha 短信验证码(需要先验证图片验证码)

返回值:

成功:
```json
{
  "status": true,
  "activeCode": ""
}
```

* 注: activeCode为激活码

失败:

```json
{
  "status": false,
  "msg": ""
}
```

</li>

<li>邮箱注册

地址: /register/email

参数:
* email 邮箱地址
* captcha 图片验证码

返回值:

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

</li>

</ol>

## 第二步:

地址: /register/step2

* 注:activeCode处为激活码,如果不存在或错误,会重定向到第一步

参数: 
* username 用户名
* pwd 密码
* activeCode 激活码

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
