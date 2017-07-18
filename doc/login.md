# 登录
**本项目提供三种登录方式**

<ol>
<li>用户名+密码+图片验证码

地址:/login/username

参数:

* username 用户名 3-20位
* pwd 密码 6-32位
* captcha 图片验证码

</li>
<li>手机号+图片验证码+短信验证码

地址:/login/phone

参数:

* phone 手机号
* captcha 图片验证码
* phone_captcha 短信验证码

注: 获取短信验证码要发送图形验证码,登录时只需要发送手机号和短信验证码即可

</li>
<li>邮箱+密码+图片验证码

地址:/login/email

参数:

* email 电子邮箱
* pwd 密码 6-32位
* captcha 图片验证码

</li>
</ol>

* 返回值:

成功:
```json
{
  "status": true,
  "token": ""
}
```
* 注: 在登录成功时会有Set-Cookie头将token信息存储到浏览器Cookie中

失败:
```json
{
  "status": false,
  "msg": ""
}
```
