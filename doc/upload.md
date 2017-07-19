# 上传文件

本项目采用统一上传文件接口

地址: /upload/ + 具体文件夹

方法: POST

返回值:

成功:
```json
{
  "status": true,
  "url": ""
}
```

失败:
```json
{
  "status": false,
  "msg": ""
}
```
